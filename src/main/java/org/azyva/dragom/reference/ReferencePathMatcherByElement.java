/*
 * Copyright 2015 AZYVA INC.
 *
 * This file is part of Dragom.
 *
 * Dragom is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Dragom is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Dragom.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.azyva.dragom.reference;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.azyva.dragom.model.ArtifactGroupId;
import org.azyva.dragom.model.ArtifactVersion;
import org.azyva.dragom.model.Model;
import org.azyva.dragom.model.Module;
import org.azyva.dragom.model.NodePath;
import org.azyva.dragom.model.Version;

/**
 * ReferencePathMatcher that matches ReferencePath's using a sequence of matching
 * elements.
 *
 * TODO: Most of this documentation should be on the project Web site to avoid
 * duplication.
 *
 * A ReferencePathMatcherByElement is a sequence of ElementMatcher's. Each
 * ElementMatcher includes matchers for matching different parts of a reference in
 * a ReferencePath. The parts that can be matched are:
 *
 * Source-level parts:
 *
 * - NodePath of the module
 * - Version of the module
 *
 * Artifact-level parts:
 *
 * - GroupId of the artifacts produced by the module
 * - ArtifactId of the artifacts produced by the module
 * - ArtifactVersion of the artifacts produced by the module
 *
 * A ElementMatcher cannot contain both source-level and artifact-level parts.
 *
 * The "*" ElementMatcher matches any one reference in a ReferencePath.
 *
 * The "**" ElementMatcher matches not only any reference, but any number
 * (including 0) of consecutive references in a ReferencePath.
 *
 * A ReferenceGaphPathMatcher is always absolute meaning that all references in
 * the ReferencePath must match starting with the first one and ending with the
 * last one. But if the first element in a ReferencePathMatcherByElement is the
 * "**" element, this effectively results in a relative
 * ReferencePathMatcherByElement.
 *
 * A part matcher can be either a fixed string which must match exactly, or a
 * regex. It can also be tete which always matches, regardless of the value of
 * the corresponding ReferencePath element part.
 *
 * A ReferencePathMatcherByElement has a literal form which is a sequence of
 * ElementMatcher literals separated with "->". Each ElementMatcher literal has
 * either of the following forms:
 *
 * - "/<module-node-path-matcher>[:<version-matcher>]" for source-level part
 *   matchers. The leading "/" differentiates from artifact-level ElementMatcher
 *   and is not part of the NodePath. But since nodes in a NodePath are separated
 *   with this same character this makes it intuitive.
 *
 * - "<group-id-matcher>[:<artifact-id-matcher>[:<artifact-version-matcher>]]" for
 *   artifact-level part matchers.
 *
 * - "**" for a "**" ElementMatcher. This notation is used for similarity with
 *   Ant-style globs.
 *
 * - "*" for a "*" ElementMatcher. This notation is used for similarity with Ant-
 *   style globs.
 *
 * A given ReferencePathMatcherByElement literal can contain a mix of source-level
 * and artifact-level ElementMatcher's. But obviously a given ElementMatcher is
 * either a source-level or an artifact-level ElementMatcher, but not both.
 *
 * The [] characters above are not literals and indicate optional parts. A part
 * which is not present always matches. An empty part always matches as well.
 * Therefore "/:", "/", "::" and ":" all match any Reference in a
 * ReferencePath and are therefore equivalent to the "*" ElementMatcher (but not
 * to the "**" ElementMatcher). The empty string (which could theoretically be
 * equivalent to the "*" ElementMatcher) is not a valid ElementMatcher.
 *
 * A part matcher is by default a fixed string, but if enclosed within "(" and ")"
 * it is treated as a Java regex (Pattern class), these characters not actually
 * being part of the regex itself. "(" and ")" are reserved characters in regexes
 * but can still be used since a ")" will be considered as a closing delimiter
 * only if followed by ":", "->" or the end of the ReferencePathMatcherByElement
 * literal.
 *
 * Because of the way the different parts of a ReferencePathMatcherByElement
 * literal are delimited, some characters and character sequences cannot be used
 * within ElementMatcher parts. They are ":" and "->". Dragom currently does not
 * support escaping them. Fortunately they are generally not used within
 * ReferencePath elements. And ":" can still be used within a regex, either as a
 * literal (escaped with "\") or as the special character, since when the opening
 * regex delimiter "(" is found, the part is delimited by the closing ")" followed
 * by ":", "->" or the end of the ReferencePathMatcherByElement literal,
 * regardless of occurrences of ":" within the regex.
 *
 * Note that a ReferencePath is, as implied by its name, a sequence of references
 * between modules. This has a few implications worth noting:
 *
 * - The first element in a ReferencePath is not a reference from a module to
 *   another module, but rather user-supplied reference to a root module. Such a
 *   reference is generally always a source-level reference, so that the first
 *   element in a ReferencePathMatcherByElement is generally always a source-level
 *   ElementMatcher;
 *
 * - A given module can produce multiple artifacts and therefore multiple
 *   artifact-level references can refer to the same module. A
 *   ReferencePathMatcherByElement that includes an artifact-level ElementMatcher
 *   will match only corresponding artifact-level references. If the module
 *   includes a reference to an artifact that is not matched by the
 *   ElementMatcher, the match is negative even though the artifact may be
 *   produced by the same module.
 *
 * Note that a ReferencePath does not include internal references within modules.
 * Some build tools such as Maven support submodules within a main module and
 * submodules (including the main module) can refer to other submodules. Dragom
 * only considers intermodule (as opposed to intramodule) references. This means
 * that a ReferencePathMatcherByElement using artifact-level ElementMatcher can
 * sometimes be confusing as only the external references are part of
 * ReferencePath's.
 *
 * Here are examples of ReferencePathMatcherByElement literals along with some
 * explanations:
 *
 * - "/Domain1/app-a": Matches the specific Module whose NodePath is
 *   "Domain1/app-a", regardless of its Version, when that Module is the first
 *   element of the ReferencePath;
 *
 * - "**->/Domain1/app-a:(D/.*)": Matches the specific Module whose NodePath is
 *   "Domain1/app-a" if its Version is dynamic, when
 *   that module is the last element of the ReferencePath (there can be any number
 *   of other elements preceding it within the ReferencePath;
 *
 * - "/Domain1/app-a->**": Matches any ReferencePath of depth 1 or more, as long
 *   as the first Module has the NodePath "Domain1/app-a";
 *
 * - "*->/Domain1/app-a->**": Matches any ReferencePath of depth 2 or more, as
 *   long as the second Module has the NodePath "Domain1/app-a";
 *
 * - "**->com.acme::(.*-SNAPSHOT)": Matches any ReferencePath of depth 1 or more,
 *   where the last reference is to an artifact having the groupId "com.acme" and
 *   a SNAPSHOT ArtifactVersion.
 *
 * @author David Raymond
 */
public class ReferencePathMatcherByElement implements ReferencePathMatcher {
	/**
	 * See description in ResourceBundle.
	 */
	private static final String MSG_PATTERN_KEY_ELEMENT_EMPTY = "ELEMENT_EMPTY";

	/**
	 * See description in ResourceBundle.
	 */
	private static final String MSG_PATTERN_KEY_ELEMENT_NOT_PROPERTY_TERMINATED = "ELEMENT_NOT_PROPERTY_TERMINATED";

	/**
	 * See description in ResourceBundle.
	 */
	private static final String MSG_PATTERN_KEY_ELEMENT_REGEX_NOT_PROPERLY_TERMINATED = "ELEMENT_REGEX_NOT_PROPERLY_TERMINATED";

	/**
	 * See description in ResourceBundle.
	 */
	private static final String MSG_PATTERN_KEY_ELEMENT_REGEX_INVALID = "ELEMENT_REGEX_INVALID";

	/**
	 * See description in ResourceBundle.
	 */
	private static final String MSG_PATTERN_KEY_ELEMENT_ARTIFACT_NO_MODULE = "ELEMENT_ARTIFACT_NO_MODULE";

	/**
	 * ResourceBundle specific to this class.
	 */
	private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(ReferencePathMatcherByElement.class.getName() + "ResourceBundle");

	/**
	 * Represents the matcher for an element of a ReferencePath.
	 *
	 * Helps match a single element of a ReferencePath.
	 */
	private static class ElementMatcher {
		/**
		 * Represents a part (NodePath, Version, groupId, artifactId or
		 * ArtifactVersion) of an element in a ElementMatcher.
		 */
		private static class PartMatcher {
			/**
			 * Set when the part is not a regex.
			 */
			private String string;

			/**
			 * Set when the part is a regex.
			 */
			private Pattern pattern;
		}

		/**
		 * Indicates if this is a "*" ElementMatcher. Mutually exclusive with all other
		 * properties below.
		 */
		private boolean indAsterisk;

		/**
		 * Indicates if this is a "**" ElementMatcher. Mutually exclusive with all other
		 * properties below.
		 */
		private boolean indDoubleAsterisk;

		/**
		 * Indicates that the ElementMatcher refers to a specific Module. This implies
		 * that either nodePath, or groupId and artifactId are specified.
		 *
		 * This is a computed property. It is used by the algorithm for inferring whether
		 * a ReferencePathMatcherByElement can potentially match children ReferencePath's.
		 */
		private boolean indSpecificModule;

		/**
		 * NodePath. Mutually exclusive with patternLiteralNodePath, isAsterisk and
		 * isDoubleAsterisk.
		 */
		private NodePath nodePath;

		/**
		 * Pattern for the NodePath literal. Mutually exclusive with nodePath,
		 * isAsterisk and isDoubleAsterisk.
		 */
		private Pattern patternLiteralNodePath;

		/**
		 * Version. Mutually exclusive with patternLiteralVersion, isAsterisk and
		 * isDoubleAsterisk.
		 */
		private Version version;

		/**
		 * Pattern for Version literal. Mutually exclusive with version, isAsterisk
		 * and isDoubleAsterisk.
		 */
		private Pattern patternLiteralVersion;

		/**
		 * GroupId. Mutually exclusive with patternGroupId, isAsterisk and
		 * isDoubleAsterisk.
		 */
		private String groupId;

		/**
		 * Pattern for the groupId. Mutually exclusive with groupId, isAsterisk and
		 * isDoubleAsterisk.
		 */
		private Pattern patternGroupId;

		/**
		 * ArtifactId. Mutually exclusive with patternArtifactId, isAsterisk and
		 * isDoubleAsterisk.
		 */
		private String artifactId;

		/**
		 * Pattern for the artifactId. Mutually exclusive with artifactId, isAsterisk and
		 * isDoubleAsterisk.
		 */
		private Pattern patternArtifactId;

		/**
		 * ArtifactVersion. Mutually exclusive with patternLiteralArtifactVersion,
		 * isAsterisk and isDoubleAsterisk.
		 */
		private ArtifactVersion artifactVersion;

		/**
		 * Pattern for the ArtifactVersion literal. Mutually exclusive with
		 * artifactVersion, isAsterisk and isDoubleAsterisk.
		 */
		private Pattern patternLiteralArtifactVersion;

		/**
		 * Parses a ElementMatcher within a ReferencePathMatcherByElement literal.
		 *
		 * The complete ReferencePathMatcherByElement is passed along with indexes to
		 * delimit the ElementMatcher so that error messages can be more contextual.
		 *
		 * @param stringReferencePathMatcherByElement Complete
		 *   ReferencePathMatcherByElement literal.
		 * @param indexStart Index of the first character within
		 *   stringReferencePathMatcherByElement that corresponds to the ElementMatcher
		 *   to parse.
		 * @param indexEnd Index just after the last character within
		 *   stringReferencePathMatcherByElement that corresponds to the ElementMatcher to
		 *   parse.
		 * @return ElementMatcher.
		 * @throws ParseException If parsing fails.
		 */
		private static ElementMatcher parse(String stringReferencePathMatcherByElement, int indexStart, int indexEnd)
		throws ParseException {
			ElementMatcher elementMatcher;
			PartMatcher partMatcher;
			int indexStartParse;

			elementMatcher = new ElementMatcher();
			partMatcher = new PartMatcher();
			indexStartParse = indexStart;

			if (indexStart == indexEnd) {
				throw new ParseException(MessageFormat.format(ReferencePathMatcherByElement.resourceBundle.getString(ReferencePathMatcherByElement.MSG_PATTERN_KEY_ELEMENT_EMPTY), stringReferencePathMatcherByElement, indexStart), indexStart);
			}

			if (stringReferencePathMatcherByElement.substring(indexStart, indexEnd).equals("*")) {
				elementMatcher.indAsterisk = true;
			} else if (stringReferencePathMatcherByElement.substring(indexStart, indexEnd).equals("**")) {
					elementMatcher.indDoubleAsterisk = true;
			} else {
				if (stringReferencePathMatcherByElement.charAt(indexStartParse) == '/') {
					indexStartParse = ElementMatcher.parsePart(stringReferencePathMatcherByElement, indexStartParse + 1, indexEnd, partMatcher); // The + 1 is to skip the leading '/'.

					if (partMatcher.string != null) {
						elementMatcher.nodePath = NodePath.parse(partMatcher.string);
					}

					elementMatcher.patternLiteralNodePath = partMatcher.pattern;

					if (indexStartParse != indexEnd) {
						indexStartParse = ElementMatcher.parsePart(stringReferencePathMatcherByElement, indexStartParse, indexEnd, partMatcher);

						if (partMatcher.string != null) {
							elementMatcher.version = Version.parse(partMatcher.string);
						}

						elementMatcher.patternLiteralVersion = partMatcher.pattern;
					}

					if (elementMatcher.nodePath != null) {
						elementMatcher.indSpecificModule = true;
					}

					// If "*" is not used, but all ElementMatcher parts are empty or
					// not specified, this is equivalent to the "*" ElementMatcher.
					if (   (elementMatcher.nodePath == null)
						&& (elementMatcher.patternLiteralNodePath == null)
						&& (elementMatcher.version == null)
						&& (elementMatcher.patternLiteralVersion == null)) {

						elementMatcher.indAsterisk = true;
					}
				} else {
					indexStartParse = ElementMatcher.parsePart(stringReferencePathMatcherByElement, indexStartParse, indexEnd, partMatcher);

					elementMatcher.groupId = partMatcher.string;
					elementMatcher.patternGroupId = partMatcher.pattern;

					if (indexStartParse != indexEnd) {
						indexStartParse = ElementMatcher.parsePart(stringReferencePathMatcherByElement, indexStartParse, indexEnd, partMatcher);

						elementMatcher.artifactId = partMatcher.string;
						elementMatcher.patternArtifactId = partMatcher.pattern;

						if (indexStartParse != indexEnd) {
							indexStartParse = ElementMatcher.parsePart(stringReferencePathMatcherByElement, indexStartParse, indexEnd, partMatcher);

							if (partMatcher.string != null) {
								elementMatcher.artifactVersion = ArtifactVersion.parse(partMatcher.string);
							}

							elementMatcher.patternLiteralArtifactVersion = partMatcher.pattern;
						}
					}

					if ((elementMatcher.groupId != null) && (elementMatcher.artifactId != null)) {
						elementMatcher.indSpecificModule = true;
					}

					// If "*" is not used, but all ElementMatcher parts are empty or not specified,
					// this is equivalent to the "*" ElementMatcher.
					if (   (elementMatcher.groupId == null)
						&& (elementMatcher.patternGroupId == null)
						&& (elementMatcher.artifactId == null)
						&& (elementMatcher.patternArtifactId == null)
						&& (elementMatcher.artifactVersion == null)
						&& (elementMatcher.patternLiteralArtifactVersion == null)) {

						elementMatcher.indAsterisk = true;
					}
				}

				// If indexStartParse != indexEnd, then it should be the case that
				// indexStartParse < indexEnd meaning that the ElementMatcher contains too many
				// parts (more than 2 for a source-level ElementMatcher or more than 3 for an
				// artifact-level ElementMatcher). It is not expected that
				// indexStartParse > indexEnd, but we consider that case in order to fail fast
				// just in case.
				if (indexStartParse != indexEnd) {
					throw new ParseException(MessageFormat.format(ReferencePathMatcherByElement.resourceBundle.getString(ReferencePathMatcherByElement.MSG_PATTERN_KEY_ELEMENT_NOT_PROPERTY_TERMINATED), stringReferencePathMatcherByElement, indexStart, indexEnd, indexStartParse), indexStartParse);
				}
			}

			return elementMatcher;
		}

		/**
		 * @return ElementMatcher literal.
		 */
		@Override
		public String toString() {
			StringBuilder stringBuilder;

			if (this.indDoubleAsterisk) {
				return "**";
			} else if (this.indAsterisk) {
				return "*";
			} else if ((this.nodePath != null) || (this.patternLiteralNodePath != null) || (this.version != null) || (this.patternLiteralVersion != null)) {
				stringBuilder = new StringBuilder();

				stringBuilder.append('/');

				if (this.nodePath != null) {
					stringBuilder.append(this.nodePath);
				} else if (this.patternLiteralNodePath != null) {
					stringBuilder.append('(').append(this.patternLiteralNodePath).append(')');
				}

				if (this.version != null) {
					stringBuilder.append(':');
					stringBuilder.append(this.version);
				} else if (this.patternLiteralVersion != null) {
					stringBuilder.append(':');
					stringBuilder.append('(').append(this.patternLiteralVersion).append(')');
				}

				return stringBuilder.toString();
			} else {
				stringBuilder = new StringBuilder();

				if (this.groupId != null) {
					stringBuilder.append(this.groupId);
				} else if (this.patternGroupId != null) {
					stringBuilder.append('(').append(this.patternGroupId).append(')');
				}

				if (this.artifactId != null) {
					stringBuilder.append(':');
					stringBuilder.append(this.artifactId);
				} else if (this.patternArtifactId != null) {
					stringBuilder.append(':');
					stringBuilder.append('(').append(this.patternArtifactId).append(')');
				}

				if (this.artifactVersion != null) {
					stringBuilder.append(':');
					stringBuilder.append(this.artifactVersion);
				} else if (this.patternLiteralArtifactVersion != null) {
					stringBuilder.append(':');
					stringBuilder.append('(').append(this.patternLiteralArtifactVersion).append(')');
				}

				return stringBuilder.toString();
			}
		}

		/**
		 * Parses the next part of a ElementMatcher literal.
		 *
		 * Parts in a ElementMatcher are separated with ":". But if a part starts with "("
		 * (indicating a regex part), then it must end with ")" followed by ":" or the end
		 * of the ElementMatcher, so that ":" may be present within "(" and ")".
		 *
		 * @param stringReferencePathMatcherByElement Complete
		 *   ReferencePathMatcherByElement literal.
		 * @param indexStart Index of the first character within
		 *   stringReferencePathMatcherByElement that corresponds to the ElementMatcher
		 *   part to parse.
		 * @param indexEnd Index just after the last character within
		 *   stringReferencePathMatcherByElement that corresponds to the ElementMatcher
		 *   part to parse.
		 * @return Index of the next next part available for parsing, or indexEnd if no
		 *   more part available.
		 * @throws ParseException If parsing fails.
		 */
		private static int parsePart(String stringReferencePathMatcherByElement, int indexStart, int indexEnd, PartMatcher partMatcher)
		throws ParseException {
			int indexPartEnd;

			if (stringReferencePathMatcherByElement.charAt(indexStart) == '(') {
				String regex;

				indexPartEnd = stringReferencePathMatcherByElement.indexOf("):", indexStart);

				if (indexPartEnd == -1) {
					if (stringReferencePathMatcherByElement.charAt(indexEnd - 1) != ')') {
						throw new ParseException(MessageFormat.format(ReferencePathMatcherByElement.resourceBundle.getString(ReferencePathMatcherByElement.MSG_PATTERN_KEY_ELEMENT_REGEX_NOT_PROPERLY_TERMINATED), stringReferencePathMatcherByElement, indexStart, indexEnd), indexEnd);
					}

					regex = stringReferencePathMatcherByElement.substring(indexStart + 1, indexEnd - 1); // + 1 to skip "(" and - 1 to exclude ")", which are only delimiters and not part of the regex itself.
					indexPartEnd = indexEnd;
				} else {
					regex = stringReferencePathMatcherByElement.substring(indexStart + 1, indexPartEnd); // + 1 to skip "(", but indexPartEnd already at ")" (which is thus excluded), which are only delimiters and not part of the regex itself.
					indexPartEnd += 2; // Skip "):".
				}

				partMatcher.string = null;

				if (regex.length() == 0) {
					partMatcher.pattern = null;
				} else {
					try  {
						partMatcher.pattern = Pattern.compile(regex);
					} catch (PatternSyntaxException pse) {
						throw new ParseException(MessageFormat.format(ReferencePathMatcherByElement.resourceBundle.getString(ReferencePathMatcherByElement.MSG_PATTERN_KEY_ELEMENT_REGEX_INVALID), stringReferencePathMatcherByElement, indexStart + 1, indexEnd, pse.getDescription(), indexStart + 1 + pse.getIndex()), indexStart + 1 + pse.getIndex());
					}
				}

				return indexPartEnd;
			} else {
				String string;

				indexPartEnd = stringReferencePathMatcherByElement.indexOf(':', indexStart);

				if ((indexPartEnd == -1) || (indexPartEnd > indexEnd)) {
					indexPartEnd = indexEnd;
				}

				string = stringReferencePathMatcherByElement.substring(indexStart, indexPartEnd);

				if (string.length() == 0) {
					partMatcher.string = null;
				} else {
					partMatcher.string = string;
				}

				partMatcher.pattern = null;

				// If we reached the end of the ElementMatcher, we return the end index to inform
				// the caller. Otherwise, we skip ":".
				if (indexPartEnd == indexEnd) {
					return indexPartEnd;
				} else {
					return indexPartEnd + 1;
				}
			}
		}

		/**
		 * Verifies if this ElementMatcher matches a Reference.
		 *
		 * This method is not expected to be called for the "**" ElementMatcher.
		 *
		 * @param reference Reference.
		 * @return true if the Reference is matched by the ElementMatcher.
		 */
		private boolean matches(Reference reference) {
			Matcher matcher;

			if (this.indDoubleAsterisk) {
				throw new RuntimeException("Method called for a \"**\" matcher.");
			}

			if (this.indAsterisk) {
				return true;
			}

			// A ElementMatcher can be source-level or an artifact-level but not both.
			// Therefore many execution paths below are mutually exclusive. We nevertheless
			// keep the code simple and linear by not introducing nested if's.

			if (this.nodePath != null) {
				if (!this.nodePath.equals(reference.getModuleVersion().getNodePath())) {
					return false;
				}
			}

			if (this.patternLiteralNodePath != null) {
				matcher = this.patternLiteralNodePath.matcher(reference.getModuleVersion().getNodePath().toString());

				if (!matcher.matches()) {
					return false;
				}
			}

			if (this.version != null) {
				if (!this.version.equals(reference.getModuleVersion().getVersion())) {
					return false;
				}
			}

			if (this.patternLiteralVersion != null) {
				matcher = this.patternLiteralVersion.matcher(reference.getModuleVersion().getVersion().toString());

				if (!matcher.matches()) {
					return false;
				}
			}

			if (this.groupId != null) {
				if (reference.getArtifactGroupId() == null) {
					return false;
				}

				if (!this.groupId.equals(reference.getArtifactGroupId().getGroupId())) {
					return false;
				}
			}

			if (this.patternGroupId != null) {
				if (reference.getArtifactGroupId() == null) {
					return false;
				}

				matcher = this.patternGroupId.matcher(reference.getArtifactGroupId().getGroupId());

				if (!matcher.matches()) {
					return false;
				}
			}

			if (this.artifactId != null) {
				if (reference.getArtifactGroupId() == null) {
					return false;
				}

				if (!this.artifactId.equals(reference.getArtifactGroupId().getArtifactId())) {
					return false;
				}
			}

			if (this.patternArtifactId != null) {
				if (reference.getArtifactGroupId() == null) {
					return false;
				}

				matcher = this.patternArtifactId.matcher(reference.getArtifactGroupId().getArtifactId());

				if (!matcher.matches()) {
					return false;
				}
			}

			if (this.artifactVersion != null) {
				if (reference.getArtifactVersion() == null) {
					return false;
				}

				if (!this.artifactVersion.equals(reference.getArtifactVersion())) {
					return false;
				}
			}

			if (this.patternLiteralArtifactVersion != null) {
				if (reference.getArtifactVersion() == null) {
					return false;
				}

				matcher = this.patternLiteralArtifactVersion.matcher(reference.getArtifactVersion().toString());

				if (!matcher.matches()) {
					return false;
				}
			}

			// If we get here it means that all parts of the ElementMatcher that are specified
			// match the Reference.
			return true;
		}

		@Override
		public boolean equals(Object other) {
			ElementMatcher elementMatcherOther;

			if (this == other) {
				return true;
			}

			if (other == null) {
				return false;
			}

			if (!(other instanceof ElementMatcher)) {
				return false;
			}

			elementMatcherOther = (ElementMatcher) other;

			if (this.indAsterisk != elementMatcherOther.indAsterisk) {
				return false;
			}

			if (this.indDoubleAsterisk != elementMatcherOther.indDoubleAsterisk) {
				return false;
			}

			if (this.indSpecificModule != elementMatcherOther.indSpecificModule) {
				return false;
			}

			if (this.nodePath == null) {
				if (elementMatcherOther.nodePath != null) {
					return false;
				}
			} else if (!this.nodePath
					.equals(elementMatcherOther.nodePath)) {
				return false;
			}

			if (this.patternLiteralNodePath == null) {
				if (elementMatcherOther.patternLiteralNodePath != null) {
					return false;
				}
			} else if (!this.patternLiteralNodePath.toString().equals(elementMatcherOther.patternLiteralNodePath.toString())) {
				return false;
			}

			if (this.version == null) {
				if (elementMatcherOther.version != null) {
					return false;
				}
			} else if (!this.version.equals(elementMatcherOther.version)) {
				return false;
			}

			if (this.patternLiteralVersion == null) {
				if (elementMatcherOther.patternLiteralVersion != null) {
					return false;
				}
			} else if (!this.patternLiteralVersion.toString().equals(elementMatcherOther.patternLiteralVersion.toString())) {
				return false;
			}

			if (this.groupId == null) {
				if (elementMatcherOther.groupId != null) {
					return false;
				}
			} else if (!this.groupId.equals(elementMatcherOther.groupId)) {
				return false;
			}

			if (this.patternGroupId == null) {
				if (elementMatcherOther.patternGroupId != null) {
					return false;
				}
			} else if (!this.patternGroupId.toString().equals(elementMatcherOther.patternGroupId.toString())) {
				return false;
			}

			if (this.artifactId == null) {
				if (elementMatcherOther.artifactId != null) {
					return false;
				}
			} else if (!this.artifactId.equals(elementMatcherOther.artifactId)) {
				return false;
			}

			if (this.patternArtifactId == null) {
				if (elementMatcherOther.patternArtifactId != null) {
					return false;
				}
			} else if (!this.patternArtifactId.toString().equals(elementMatcherOther.patternArtifactId.toString())) {
				return false;
			}

			if (this.artifactVersion == null) {
				if (elementMatcherOther.artifactVersion != null) {
					return false;
				}
			} else if (!this.artifactVersion.equals(elementMatcherOther.artifactVersion)) {
				return false;
			}

			if (this.patternLiteralArtifactVersion == null) {
				if (elementMatcherOther.patternLiteralArtifactVersion != null) {
					return false;
				}
			} else if (!this.patternLiteralArtifactVersion.toString().equals(elementMatcherOther.patternLiteralArtifactVersion.toString())) {
				return false;
			}

			return true;
		}
	}

	/**
	 * Used by the matches method to keep track of the ElementMatcher groups.
	 */
	private static class ElementMatcherGroup {
		/**
		 * Index of first element in the group.
		 */
		private int indexFirstElementMatcher;

		/**
		 * Number of elements in the group.
		 */
		private int nbElementMatchers;

		/**
		 * Total number of ElementMatcher's remaining including those in this group and
		 * those in the following groups.
		 */
		private int nbTotalElementMatchers;
	}

	/**
	 * Used by the canMatchChildren method to convert a
	 * ReferencePathMatcherByElement to a sequence of specific Module matchers.
	 */
	private static class ModuleMatcher {
		/**
		 * NodePath.
		 */
		NodePath nodePath;

		/**
		 * Minimum number of preceding elements.
		 */
		int minPrecedingElements;

		/**
		 * Maximum number of preceding elements. -1 if no maximum (if the "**"
		 * ElementMatcher was used).
		 */
		int maxPrecedingElements;

		/**
		 * Index of the ElementMatcher to which this entry corresponds.
		 */
		int indexElementMatcher;
	}

	/**
	 * Model. Needed to convert {@link ArtifactGroupId}'s to {@link NodePath}'s.
	 */
	private Model model;

	/**
	 * List of ElementMatcher that make up this ReferencePathMatcherByElement.
	 */
	private List<ElementMatcher> listElementMatcher;

	/**
	 * Indicates the ReferencePathMatcherByElement has a fixed length (does not
	 * contain "**" ElementMatcher's).
	 */
	private boolean indFixedLength;

	/**
	 * Length of the fixed part (excluding the "**" ElementMatcher) of the
	 * ReferencePathMatcherByElement.
	 */
	private int fixedLength;

	/**
	 * Pre-computed index of the first "**" ElementMatcher.
	 *
	 * If the ReferencePathMatcherByElement is fixed (no "**" ElementMatcher), this is
	 * set to the end of the List of ElementMatcher to make the implementation of the
	 * matches method simpler.
	 *
	 * Used by the matches method to keep track of the ElementMatcherGroup's.
	 */
	private int indexFirstDoubleAsterisk;

	/**
	 * Pre-computed index of the last "**" ElementMatcher if !indFixedLength.
	 *
	 * Used by the matches method to keep track of the ElementMatcherGroup's.
	 */
	private int indexLastDoubleAsterisk;

	/**
	 * Pre-computed List of ElementMatcherGroup if !indFixedLength.
	 *
	 * Used by the matches method to keep track of the ElementMatcherGroup's.
	 */
	private List<ElementMatcherGroup> listElementMatcherGroup;

	/**
	 * Pre-computed List of ModuleMatcher.
	 *
	 * Used by the canMatchChildren method to convert a
	 * ReferencePathMatcherByElement to a sequence of specific Module matchers.
	 */
	private List<ModuleMatcher> listModuleMatcher;

	/**
	 * Private default constructor used only internally.
	 */
	private ReferencePathMatcherByElement() {
	}

	/**
	 * Constructor using a ReferencePathMatcherByElement literal.
	 * <p>
	 * Throws RuntimeException if parsing fails.
	 *
	 * @param stringReferencePathMatcherByElement ReferencePathMatcherByElement
	 *   literal.
	 * @param model Model. The {@link Model} is required since a
	 *   ReferencePathMatcherByElement can refer to {@link ArtifactGroupId}'s which
	 *   must be translated to {@link NodePath}'s.
	 */
	public ReferencePathMatcherByElement(String stringReferencePathMatcherByElement, Model model) {
		int indexStartElementMatcher;
		int indexEndElementMatcher;

		try {
			this.model = model;
			this.listElementMatcher = new ArrayList<ElementMatcher>();
			this.indFixedLength = true;

			// First perform the actual parsing.

			indexStartElementMatcher = 0;

			do {
				ElementMatcher elementMatcher;

				indexEndElementMatcher = stringReferencePathMatcherByElement.indexOf("->", indexStartElementMatcher);

				if (indexEndElementMatcher == -1) {
					indexEndElementMatcher = stringReferencePathMatcherByElement.length();
				}

				elementMatcher = ElementMatcher.parse(stringReferencePathMatcherByElement, indexStartElementMatcher, indexEndElementMatcher);

				if (elementMatcher.indDoubleAsterisk) {
					this.indFixedLength = false;
				} else {
					this.fixedLength++;
				}

				this.listElementMatcher.add(elementMatcher);

				indexStartElementMatcher = indexEndElementMatcher + 2; // + 2 to skip "->", if any.
			} while (indexEndElementMatcher != stringReferencePathMatcherByElement.length());

			// Then initialize the transient fields.

			this.init();
		} catch (ParseException pe) {
			throw new RuntimeException(pe);
		}
	}

	/**
	 * Parses a ReferencePathMatcherByElement literal.
	 *
	 * @param stringReferencePathMatcherByElement ReferencePathMatcherByElement
	 *   literal.
	 * @param model Model. The {@link Model} is required since a
	 *   ReferencePathMatcherByElement can refer to {@link ArtifactGroupId}'s which
	 *   must be translated to {@link NodePath}'s.
	 * @return ReferencePathMatcherByElement.
	 * @throws ParseException If parsing fails.
	 */
	public static ReferencePathMatcherByElement parse(String stringReferencePathMatcherByElement, Model model)
	throws ParseException {
		try {
			return new ReferencePathMatcherByElement(stringReferencePathMatcherByElement, model);
		} catch (RuntimeException re) {
			if (re.getCause() instanceof ParseException) {
				throw (ParseException)re.getCause();
			} else {
				throw re;
			}
		}
	}

	/**
	 * Factors out the initialization code for the pre-computed fields since it it
	 * used by both the parse and canMatchChildren methods.
	 *
	 * @param model Model.
	 * @throws ParseException If pre-computed fields cannot be computed because of
	 *   invalid data parsed.
	 */
	private void init()
	throws ParseException {
		ModuleMatcher moduleMatcher;
		int index;

		// Pre-compute indexFirstDoubleAsterisk, indexLastDoubleAsterisk and
		// listElementMatcherGroup.

		for (index = 0; (index < this.listElementMatcher.size()) && !this.listElementMatcher.get(index).indDoubleAsterisk; index++);

		if (index != this.listElementMatcher.size()) {
			this.indexFirstDoubleAsterisk = index;

			// Here we iterate from the end. If we get here, we know for sure the
			// ReferencePath is not fixed and therefore we do not expect the index to cross
			// indexFirstDoubleAsterisk.
			for (index = this.listElementMatcher.size() - 1; (index >= 0) && !this.listElementMatcher.get(index).indDoubleAsterisk; index--);

			if (index < this.indexFirstDoubleAsterisk) {
				throw new RuntimeException("Must not get here.");
			}

			this.indexLastDoubleAsterisk = index;
		} else {
			this.indexFirstDoubleAsterisk = this.listElementMatcher.size();
		}

		this.listElementMatcherGroup = new ArrayList<ElementMatcherGroup>();

		// We build a List of ElementMatcherGroup. Each element in this List represents a
		// distinct sequence of ElementMatcher's preceded by a "**" ElementMatcher and
		// whose match position within the ReferencePath can be adjusted in order to
		// obtain a match over the complete ReferenceGrathPath. We exclude the last "**"
		// ElementMatcher since we know for sure it exists and that the last
		// ElementMatcher group is surrounded by a "**" ElementMatcher and its position
		// can be adjusted. The position of the ElementMatcher's after the last "**"
		// ElementMatcher cannot be adjusted so these ElementMatcher's are not included in
		// a ElementMatcherGroup.
		for (index = this.indexFirstDoubleAsterisk; index < this.indexLastDoubleAsterisk; index++) {
			ElementMatcherGroup elementMatcherGroup;

			if (this.listElementMatcher.get(index).indDoubleAsterisk) {
				elementMatcherGroup = new ElementMatcherGroup();

				elementMatcherGroup.indexFirstElementMatcher = index + 1;

				this.listElementMatcherGroup.add(elementMatcherGroup);
			} else {
				elementMatcherGroup = this.listElementMatcherGroup.get(this.listElementMatcherGroup.size() - 1);

				elementMatcherGroup.nbElementMatchers++;

				for (ElementMatcherGroup elementMatcherGroup2: this.listElementMatcherGroup) {
					elementMatcherGroup2.nbTotalElementMatchers++;
				}
			}
		}

		// Pre-compute listModuleMatcher.

		this.listModuleMatcher = new ArrayList<ModuleMatcher>();
		moduleMatcher = null;

		for (index = 0; index < this.listElementMatcher.size(); index++) {
			ElementMatcher elementMatcher;

			if (moduleMatcher == null) {
				moduleMatcher = new ModuleMatcher();
			}

			elementMatcher = this.listElementMatcher.get(index);

			if (elementMatcher.indDoubleAsterisk) {
				moduleMatcher.maxPrecedingElements = -1;
			} else if (elementMatcher.indAsterisk) {
				moduleMatcher.minPrecedingElements++;
			} else if (elementMatcher.indSpecificModule) {
				// Only a ElementMatcher for a specific Module triggers the insertion of a
				// ModuleMatcher in the List.

				if (elementMatcher.nodePath != null) {
					moduleMatcher.nodePath = elementMatcher.nodePath;
				} else {
					ArtifactGroupId artifactGroupId;
					Module module;

					// If the specific Module is actually an artifact reference, it must be converted
					// into a NodePath as it is cycles between Module's that are pertinent here
					// (multiple artifacts can be produced by the same Module).

					artifactGroupId = new ArtifactGroupId(elementMatcher.groupId, elementMatcher.artifactId);
					module = this.model.findModuleByArtifactGroupId(artifactGroupId);

					if (module == null) {
						throw new ParseException(MessageFormat.format(ReferencePathMatcherByElement.resourceBundle.getString(ReferencePathMatcherByElement.MSG_PATTERN_KEY_ELEMENT_ARTIFACT_NO_MODULE), this, elementMatcher, artifactGroupId), 0);
					}

					moduleMatcher.nodePath = module.getNodePath();
				}

				moduleMatcher.indexElementMatcher = index;

				this.listModuleMatcher.add(moduleMatcher);

				moduleMatcher = null;
			} else {
				// This is for all other cases of ElementMatcher that use regexes or otherwise do
				// not refer to a specific Module.
				moduleMatcher.minPrecedingElements++;
			}
		}

		// It can happen that the ReferencePathMatcherByElement is not terminated by a
		// ElementMatcher for a specific Module. In that case the last ModuleMatcher being
		// built will not be added to the List of ModuleMatcher. This is OK as we are only
		// interested in knowing the number of preceding elements.
	}

	/**
	 * @return ReferencePathMatcherByElement literal.
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder;

		stringBuilder = new StringBuilder();

		for(ElementMatcher elementMatcher: this.listElementMatcher) {
			if (stringBuilder.length() != 0) {
				stringBuilder.append("->");
			}

			stringBuilder.append(elementMatcher.toString());
		}

		return stringBuilder.toString();
	}

	/**
	 * Verifies if a ReferencePathMatcherByElement matches a ReferencePath.
	 *
	 * A ReferencePathMatcherByElement matches a ReferencePath if all the elements in the
	 * ReferencePath are matched by all the ElementMatcher's, also taking into
	 * consideration the "*" and "**" ElementMatcher's.
	 *
	 * @param referencePath ReferencePath.
	 * @return true if the ReferencePath is matched by the
	 *   ReferencePathMatcherByElement.
	 */
	@Override
	public boolean matches(ReferencePath referencePath) {
		int index;
		int indexElement;

		if (this.indFixedLength && (this.listElementMatcher.size() != referencePath.size())) {
			return false;
		}

		if (this.fixedLength > referencePath.size()) {
			return false;
		}

		// this.indexFirstDoubleAsterisk refers to the end of the List of ElementMatcher's
		// if the ReferencePathMatcherByElement is fixed.
		for (index = 0; index < this.indexFirstDoubleAsterisk; index++) {
			if (!this.listElementMatcher.get(index).matches(referencePath.get(index))) {
				return false;
			}
		}

		// If we get to the last ElementMatcher it means the ReferencePathMatcherByElement
		// is fixed (no "**" ElementMatcher) and the ReferencePath contains the same
		// number of elements. We therefore have a match.
		if (index == this.listElementMatcher.size()) {
			return true;
		}

		// If we get here, there is at least one "**" ElementMatcher and
		// this.indexLastDoubleAsterisk is set.

		// Here we use a 0-based index, but we iterate from the end. This is required
		// since the number of ReferencePathMatcherByElement's is not necessarily the same
		// as the number of ReferencePath elements to match.
		for (index = 0; this.listElementMatcher.size() - index - 1 > this.indexLastDoubleAsterisk; index++) {
			if (!this.listElementMatcher.get(this.listElementMatcher.size() - index - 1).matches(referencePath.get(referencePath.size() - index - 1))) {
				return false;
			}
		}

		// If we get to the point where index == indexFirstDoubleAsterisk it means there
		// is only one "**" ElementMatcher. Since both the elements before and after the
		// "**" ElementMatcher were matched, we have a match.
		if (index == this.indexFirstDoubleAsterisk) {
			return true;
		}

		// We now need to match each fixed ElementMatcher within the ReferencePath
		// starting at the element following the first "**" ElementMatcher.
		indexElement = this.indexFirstDoubleAsterisk + 1;

		next_element_matcher_group:
		for (ElementMatcherGroup elementMatcherGroup: this.listElementMatcherGroup) {
			match_element_matcher_group:
			for (; indexElement + elementMatcherGroup.nbTotalElementMatchers <= this.indexLastDoubleAsterisk; indexElement++) {
				for (index = 0; index < elementMatcherGroup.nbElementMatchers; index++) {
					// If the current element in the ReferencePath is not matched by the corresponding
					// element in the ElementMatcherGroup, we need to move the match point to the next
					// element in the ReferencePath.
					if (!this.listElementMatcher.get(elementMatcherGroup.indexFirstElementMatcher + index).matches(referencePath.get(indexElement + index))) {
						continue match_element_matcher_group;
					}
				}

				// If we get here it means all the elements in the ElementMatcherGroup were
				// matched. We therefore need to point to the next element in the ReferencePath
				// following the last one matched by the ElementMatcherGroup and move on to the
				// next ElementMatcherGroup.
				indexElement += elementMatcherGroup.nbElementMatchers;
				continue next_element_matcher_group;
			}

			// If we get here it means we got to the end of the elements in the ReferencePath
			// and that there is no way to make the current ElementMatcherGroup match. We
			//therefore do not have a match.
			return false;
		}

		// If we get here, all the ElementMatcherGroup's where matched somewhere within
		// the ReferencePath. We therefore have a match.
		return true;
	}

	/**
	 * Verifies if a ReferencePathMatcherByElement can potentially match children of a
	 * ReferencePath.
	 * <p>
	 * In many cases this is obvious. ReferencePathMatcherByElement X -> Y will not
	 * match any children of ReferencePath A -> B.
	 * <p>
	 * But the rule that forbids any Module cycle within a reference graph allows us
	 * to determine in certain cases that the children of a ReferencePath cannot be
	 * matched by a ReferencePathMatcherByElement. For example
	 * ReferencePathMatcherByElement ** -> B -> A cannot match children of
	 * ReferencePath A -> B since this would imply a cycle A -> B -> A which is not
	 * permitted. There are many other more subtle cases that are covered by this
	 * method such as ReferencePathMatcherByElement A -> ** -> B -> ** -> C that can
	 * match children of A -> E, but not A -> C.
	 * <p>
	 * This is used for optimizing the traversal of reference graphs (traversal
	 * avoidance).
	 * <p>
	 * Note that when regexes are used within a ElementMatcher for the Module, it is
	 * not always possible to determine if children of a ReferencePath can be matched
	 * or not. In that case, the method returned that children can be matched, forcing
	 * the caller to perform the traversal and apply the
	 * ReferencePathMatcherByElement's.
	 *
	 * @param referencePath ReferencePath.
	 * @return true if the ReferencePath is matched by the
	 *   ReferencePathMatcherByElement.
	 */
	@Override
	public boolean canMatchChildren(ReferencePath referencePath) {
		ReferencePath referencePathCopy;
		int indexModuleMatcher;
		ModuleMatcher moduleMatcher;
		int indexTrailingElementMatcher;
		int indexElement;
		int nbFollowingElements;
		ReferencePathMatcherByElement referencePathMatcherByElementPrefix;
		ElementMatcher elementMatcherDoubleAsterisk;

		referencePathCopy = new ReferencePath(referencePath);

		// At the end of the loop below we want indexTrailingElementMatcher to refer to
		// the ElementMatcher following the one corresponding to the last ModuleMatcher
		// for which a specific Module was found in the ReferencePath. We start with 0
		// since if no ModuleMatcher corresponds to a specific Module, that ElementMatcher
		// is the first one.
		indexTrailingElementMatcher = 0;

		next_module_matcher:
		for (indexModuleMatcher = 0; indexModuleMatcher < this.listModuleMatcher.size(); indexModuleMatcher++) {
			moduleMatcher = this.listModuleMatcher.get(indexModuleMatcher);

			for (indexElement = 0; indexElement < referencePathCopy.size(); indexElement++) {
				// During the iteration among the elements in the ReferencePath, we ensure that
				// the number of unmatched elements (for a specific Module) does not exceed the
				// maximum number of preceding elements. If that becomes the cas, no children can
				// be matched.
				if ((moduleMatcher.maxPrecedingElements != -1) && (indexElement > moduleMatcher.maxPrecedingElements)) {
					return false;
				}

				if (referencePathCopy.get(indexElement).getModuleVersion().getNodePath().equals(moduleMatcher.nodePath)) {
					// If a specific Module is present in ReferencePath, we check if the number of
					// preceding elements in the ReferencePathMatcherByElement can match those in the
					// ReferencePath. If not, the ReferencePathMatcher does not match the
					// ReferencePath and will not match any of its children.
					// If a specific Module is present in ReferencePath, we check if the
					// ReferencePathMatcher can match at least the preceding elements in the
					// ReferencePath. The maximum will have been checked above.
					if (indexElement < moduleMatcher.minPrecedingElements) {
						return false;
					}

					// We need to eliminate the elements in the ReferencePath up to and including the
					// one that is matched by the specific Module in the
					// ReferencePathMatcherByElement.
					// This is the idiom for removing range of elements in a List, and ReferencePath
					// simply extends ArrayList.
					referencePathCopy.removeRootReferences(indexElement + 1);

					indexTrailingElementMatcher = moduleMatcher.indexElementMatcher + 1;

					// When a specific Module is found in the ReferencePath, we move on to the next
					// ModuleMatcher.
					continue next_module_matcher;
				}
			}

			// We also check for exceeding the maximum number of unmatched elements at the end
			// when no more element is matched.
			if ((moduleMatcher.maxPrecedingElements != -1) && (indexElement > moduleMatcher.maxPrecedingElements)) {
				return false;
			}

			// If we get here it means the specific Module is not found in the ReferencePath.
			// In that case we must exit the loop and go to the next step of the algorithm.
			break;
		}

		// Here indexModuleMatcher refers to the first ModuleMatcher that did not match or
		// to the end of the List of ModuleMatcher if they all matched.
		// If there are remaining ModuleMatcher's we must ensure that no one refers to a
		// specific Module that is present in the original ReferencePath. Otherwise (if
		// these Modules were not eliminated from the working ReferencePath), no children
		// can match as this would involve a cycle.
		while (indexModuleMatcher < this.listModuleMatcher.size()) {
			for (indexElement = 0; indexElement < referencePath.size(); indexElement++) {
				if (referencePath.get(indexElement).getModuleVersion().getNodePath().equals(this.listModuleMatcher.get(indexModuleMatcher).nodePath)) {
					return false;
				}
			}

			indexModuleMatcher++;
		}

		// Here indexTrailingElementMatcher refers to the ElementMatcher following the
		// last ModuleMatcher corresponding to a specific Module in the ReferencePath.
		// We must ensure that these trailing ElementMatcher's are such that they can
		// match deep enough to reach the children of the ReferencePath, starting at the
		// element following the last one to be matched by that last ModuleMatcher. That
		// element is the one in referencePathCopy.
		// Note that indexTrailingElementMatcher can refer to the end of the List of
		// ElementMatcher. In such a case, no iteration will be performed,
		// nbFollowingElements will remain 0 and the method will return false (no children
		// match possible).

		nbFollowingElements = 0;

		for (int index = indexTrailingElementMatcher; index < this.listElementMatcher.size(); index++) {
			if (this.listElementMatcher.get(index).indDoubleAsterisk) {
				nbFollowingElements = -1;
				break;
			} else {
				nbFollowingElements++;
			}
		}

		if ((nbFollowingElements != -1) && (nbFollowingElements <= referencePathCopy.size())) {
			return false;
		}

		// Now we know that the ReferencePathMatcherByElement can potentially match
		// children of the ReferencePath: No cycle would be introduced and it is deep
		// enough. But all the tests above have been done by matching the NodePath of the
		// ElementMatcher's referring to specific Module's. Before concluding that the
		// ReferencePathMatcherByElement can match children of the ReferencePath we at
		// least verify that the current ReferencePath is matched by the ElementMatcher's
		// up to the last ElementMatcher that referred to a specific Module. We do this by
		// invoking the matches method with a modified ReferencePathMatcherByElement that
		// includes a "**" ElementMatcher after the last ElementMatcher.
		// The problem is that ReferencePathMatcherByElement implement value-based
		// semantics (instances are immutable). But we are in this class so we allow
		// ourselves to build a temporary ReferencePathMatcherByElement that suits our
		// purpose.
		referencePathMatcherByElementPrefix = new ReferencePathMatcherByElement();
		referencePathMatcherByElementPrefix.model = this.model;
		referencePathMatcherByElementPrefix.listElementMatcher = new ArrayList<ElementMatcher>(this.listElementMatcher.subList(0, indexTrailingElementMatcher));
		elementMatcherDoubleAsterisk = new ElementMatcher();
		elementMatcherDoubleAsterisk.indDoubleAsterisk = true;
		referencePathMatcherByElementPrefix.listElementMatcher.add(elementMatcherDoubleAsterisk);

		try {
			referencePathMatcherByElementPrefix.init();
		} catch (ParseException pe) {
			// We do not expect to catch ParseException here since the data comes from the
			// original ReferencePathMatcherByElement which has already been valided.
			throw new RuntimeException(pe);
		}

		return referencePathMatcherByElementPrefix.matches(referencePath);
	}

	@Override
	public boolean equals(Object other) {
		ReferencePathMatcherByElement referencePathMatcherByElementOther;

		if (this == other) {
			return true;
		}

		if (!(other instanceof ReferencePathMatcherByElement)) {
			return false;
		}

		referencePathMatcherByElementOther = (ReferencePathMatcherByElement)other;

		return this.listElementMatcher.equals(referencePathMatcherByElementOther.listElementMatcher);
	}
}
