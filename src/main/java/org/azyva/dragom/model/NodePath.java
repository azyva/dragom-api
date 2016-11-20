/*
 * Copyright 2015 - 2017 AZYVA INC. INC.
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

package org.azyva.dragom.model;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the path of a {@link Node} within the {@link Model}.
 * <p>
 * Includes methods to manipulate and get information about NodePath's.
 * <p>
 * Implements value semantics. Instances are immutable.
 * <p>
 * Implements {@link hashCode} and {@link equals} so that instances can be used as
 * Map keys.
 * </p>
 * A NodePath is a sequence of Node names. All of these Node's are
 * {@link ClassificationNode}'s except the last one which may be a {@link Module}.
 * In that case the NodePath is said to not be partial (complete). If the last
 * Node is a ClassificationNode, it is said to be partial. This is similar to
 * files and directories in a file system, although there is no relation to actual
 * files and directories.
 * <p>
 * In general when we talk about a NodePath we mean a non-partial (complete)
 * NodePath that refers to a Module. When a NodePath is partial we make it
 * explicit.
 * <p>
 * As a convention, ClassificationNode's are named using the PascalCase notation,
 * while Module's are named using the lower-case-with-dashes notation. But this is
 * not a requirement and is not enforced.
 * <p>
 * Node names can contain characters A - Z, a - z, 0 - 9, - and _. The character
 * . is not allowed. Node names must contain at least 1 character. The first
 * character of a Node name cannot be - (but can be _).
 * <p>
 * NodePath supports a literal form where "/" is used to separate Node names, as
 * in Domain/SubDomain/System/my-module. A partial NodePath ends with a trailing
 * "/".
 *
 * The empty NodePath literal represents the root ClassificationNode (a partial
 * NodePath). Contrary to other partial NodePath's, it does not end with "/" since
 * this may be confusing as NodePath's never begin with "/". So the empty NodePath
 * literal is a special case.
 * <p>
 * A NodePath is always absolute, the first Node representing an immediate child
 * of the unnamed root ClassificationNode of the hierarchy. A NodePath does not
 * start with a "/"; emphasis on them being absolute is not deemed necessary.
 *
 * @author David Raymond
 */
public final class NodePath {
	/**
	 * See description in ResourceBundle.
	 */
	private static final String MSG_PATTERN_KEY_NODE_PATH_PARSING_ERROR = "NODE_PATH_PARSING_ERROR";

	/**
	 * ResourceBundle specific to this class.
	 */
	private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(NodePath.class.getName() + "ResourceBundle");

	/**
	 * Pattern for validating a NodePath literal.
	 * <p>
	 * Not used for actually parsing.
	 */
	private static final Pattern patternValidateNodePathLiteral = Pattern.compile("(?:[A-Za-z0-9_][A-Za-z0-9\\-_]*/)*(?:[A-Za-z0-9_][A-Za-z0-9\\-_]*)?");

	/**
	 * Pattern for validating a {@link Node} names.
	 */
	static private Pattern patternValidateNodeName = Pattern.compile("[A-Za-z0-9_][A-Za-z0-9\\-_]*");

	/**
	 * Array of node names.
	 */
	private String[] arrayNodeName;

	/**
	 * Indicates if the NodePath is partial.
	 */
	private boolean isPartial;

	/**
	 * Constructor for a non-partial NodePath using an array of {@link Node} names.
	 *
	 * @param arrayNodeName Array of {@link Node} names.
	 */
	public NodePath(String[] arrayNodeName) {
		this(arrayNodeName, false);
	}

	/**
	 * Constructor using an array of {@link Node} names.
	 *
	 * @param arrayNodeName Array of {@link Node} names.
	 * @param isPartial Indicates if the NodePath is partial.
	 */
	public NodePath(String[] arrayNodeName, boolean isPartial) {
		if ((arrayNodeName.length == 0) && !isPartial) {
			throw new RuntimeException("A NodePath cannot be empty.");
		}

		for (String nodeName : arrayNodeName) {
			if (!NodePath.validateNodeName(nodeName)) {
				throw new RuntimeException("Node name " + nodeName + " is invalid.");
			}
		}

		// We make a copy to ensure immutability.
		this.arrayNodeName = Arrays.copyOf(arrayNodeName, arrayNodeName.length);
		this.isPartial = isPartial;
	}

	/**
	 * Constructor for a non-partial NodePath using a partial parent NodePath and a
	 * {@link Module} name.
	 *
	 * @param nodePathParent Parent NodePath (partial).
	 * @param moduleName Module name.
	 */
	public NodePath(NodePath nodePathParent, String moduleName) {
		this(nodePathParent, moduleName, false);
	}

	/**
	 * Constructor using a partial parent NodePath and a {@link Node} name.
	 *
	 * @param nodePathParent Parent NodePath (partial).
	 * @param nodeName Node name.
	 * @param isPartial Indicates if the NodePath is partial.
	 */
	public NodePath(NodePath nodePathParent, String nodeName, boolean isPartial) {
		if (!NodePath.validateNodeName(nodeName)) {
			throw new RuntimeException("Node name " + nodeName + " is invalid.");
		}

		if (nodePathParent != null) {
			if (!nodePathParent.isPartial()) {
				throw new RuntimeException("A node cannot be appended to a complete NodsePath " + nodePathParent + '.');
			}

			this.arrayNodeName = new String[nodePathParent.arrayNodeName.length + 1];
			System.arraycopy(nodePathParent.arrayNodeName, 0, this.arrayNodeName, 0, nodePathParent.arrayNodeName.length);
			this.arrayNodeName[this.arrayNodeName.length - 1] = nodeName;
			this.isPartial = isPartial;
		} else {
			this.arrayNodeName = new String[1];
			this.arrayNodeName[0] = nodeName;
			this.isPartial = isPartial;
		}
	}

	/**
	 * Constructor used internally.
	 * <p>
	 * Used by {@link #getNodePathParent}.
	 */
	private NodePath() {
	}

	/**
	 * Constructor using a NodePath literal.
	 * <p>
	 * Throws RuntimeException if parsing fails.
	 *
	 * @param stringNodePath NodePath literal.
	 */
	public NodePath(String stringNodePath) {
		// We handle the case where the NodePath literal is the empty string separately.
		if (stringNodePath.isEmpty()) {
			this.isPartial = true;
			this.arrayNodeName = new String[0];
		} else {
			try {
				Matcher matcher;

				matcher = NodePath.patternValidateNodePathLiteral.matcher(stringNodePath);

				if (!matcher.matches()) {
					throw new ParseException(MessageFormat.format(NodePath.resourceBundle.getString(NodePath.MSG_PATTERN_KEY_NODE_PATH_PARSING_ERROR), stringNodePath, NodePath.patternValidateNodePathLiteral), 0);
				}

				if (stringNodePath.charAt(stringNodePath.length() - 1) == '/') {
					this.isPartial = true;
					stringNodePath = stringNodePath.substring(0, stringNodePath.length() - 1);
				} else {
					this.isPartial = false;
				}

				this.arrayNodeName = stringNodePath.split("/");

				// arrayNodeName cannot be empty here, even if stringNodePath is
				// the empty String. This would have been caught above.
			} catch (ParseException pe) {
				throw new RuntimeException(pe);
			}
		}
	}

	/**
	 * Parses a NodePath literal.
	 *
	 * @param stringNodePath
	 * @return NodePath
	 * @throws ParseException If parsing fails.
	 */
	public static NodePath parse(String stringNodePath)
	throws ParseException {
		try {
			return new NodePath(stringNodePath);
		} catch (RuntimeException re) {
			if (re.getCause() instanceof ParseException) {
				throw (ParseException)re.getCause();
			} else {
				throw re;
			}
		}
	}

	/**
	 * @return Array of {@link Node} names.
	 */
	public String[] getArrayNodeName() {
		// A copy is returned to ensure immutability.
		return Arrays.copyOf(this.arrayNodeName,  this.arrayNodeName.length);
	}

	public boolean isPartial() {
		return this.isPartial;
	}

	/**
	 * @return Number of {@link Node}'s in the NodePath.
	 */
	public int getNodeCount() {
		return this.arrayNodeName.length;
	}

	/**
	 * Returns the name of a {@link Node} in the NodePath.
	 *
	 * @param index Index of the Node.
	 * @return Name of the Node.
	 */
	public String getNodeName(int index) {
		if (index >= this.arrayNodeName.length) {
			throw new RuntimeException("The index " + index + " is larger than the index of the last node of the NodsePath " + this + '.');
		}

		return this.arrayNodeName[index];
	}

	/**
	 * @return Partial parent NodePath of this NodePath.
	 */
	public NodePath getNodePathParent() {
		NodePath nodePathParent;

		nodePathParent = new NodePath();

		nodePathParent.arrayNodeName = Arrays.copyOfRange(this.arrayNodeName,  0,  this.arrayNodeName.length - 1);
		nodePathParent.isPartial = true;

		return nodePathParent;
	}

	/**
	 * @return If partial, this NodePath else its partial parent NodePath.
	 */
	public NodePath getNodePathPartial() {
		if (this.isPartial) {
			return this;
		} else {
			return this.getNodePathParent();
		}
	}

	/**
	 * @return {@link Module} name of this NodePath. The NodePath must not be partial.
	 */
	public String getModuleName() {
		if (this.isPartial) {
			throw new RuntimeException("The NodsePath " + this + " is partial and has no module.");
		}

		return this.arrayNodeName[this.arrayNodeName.length - 1];
	}

	/**
	 * @return NodePath literal.
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder;

		stringBuilder = new StringBuilder();

		for (String nodeName : this.arrayNodeName) {
			stringBuilder.append(nodeName).append("/");
		}

		if (!this.isPartial) {
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		}

		return stringBuilder.toString();
	}

	/**
	 * This method behaves in a way very similar to toString, except that '.' is used
	 * to separate node names instead of "/", and no trailing '.' is added at the end,
	 * making the returned String more appropriate for use as part of a property name.
	 *
	 * @return See description.
	 */
	public String getPropertyNameSegment() {
		StringBuilder stringBuilder;

		stringBuilder = new StringBuilder();

		for (String nodeName : this.arrayNodeName) {
			stringBuilder.append(nodeName).append('.');
		}

		stringBuilder.deleteCharAt(stringBuilder.length() - 1);

		return stringBuilder.toString();
	}

	/**
	 * Validates a node name.
	 * <p>
	 * Used by {@link #parse}.
	 *
	 * @param nodeName Name of the node.
	 * @return true if node name is valid, false if not.
	 */
	private static boolean validateNodeName(String nodeName) {
		Matcher matcher;

		matcher = NodePath.patternValidateNodeName.matcher(nodeName);

		return matcher.matches();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result;

		result = 1;
		result = (prime * result) + Arrays.deepHashCode(this.arrayNodeName);
		result = (prime * result) + (this.isPartial ? 1 : 0);

		return result;
	}

	@Override
	public boolean equals(Object other) {
		NodePath nodePathOther;

		if (this == other) {
			return true;
		}

		if (!(other instanceof NodePath)) {
			return false;
		}

		nodePathOther = (NodePath)other;

		return (Arrays.equals(this.arrayNodeName, nodePathOther.arrayNodeName) && (this.isPartial == nodePathOther.isPartial));
	}
}
