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

package org.azyva.dragom.reference;

import java.util.Map;

import org.azyva.dragom.model.Model;
import org.azyva.dragom.model.Module;
import org.azyva.dragom.model.ModuleVersion;
import org.azyva.dragom.model.Version;
import org.azyva.dragom.model.plugin.ScmPlugin;

/**
 * ReferencePathMatcher that matches ReferencePath's based on a {@link Version}
 * attribute defined on {@link Version} the leaf {@link Reference}.
 *
 * @author David Raymond
 */
public class ReferencePathMatcherVersionAttribute implements ReferencePathMatcher {
	/**
	 * Name of the {@link Version} attribute.
	 */
	private String versionAttributeName;

	/**
	 * Value of the {@link Version} attribute to be matched.
	 */
	private String versionAttributeValue;

	/**
	 * Model. Needed to access version attributes.
	 */
	private Model model;

	/**
	 * Constructor.
	 */
	public ReferencePathMatcherVersionAttribute(String versionAttributeName, String versionAttributeValue, Model model) {
		this.versionAttributeName = versionAttributeName;
		this.versionAttributeValue = versionAttributeValue;
		this.model = model;
	}

	@Override
	public boolean matches(ReferencePath referencePath) {
		ModuleVersion moduleVersion;
		Module module;
		ScmPlugin scmPlugin;
		Map<String, String> mapVersionAttr;
		String versionAttributeValueFound;

		moduleVersion = referencePath.getLeafModuleVersion();
		module = this.model.getModule(moduleVersion.getNodePath());
		scmPlugin = module.getNodePlugin(ScmPlugin.class, null);
		mapVersionAttr = scmPlugin.getMapVersionAttr(moduleVersion.getVersion());
		versionAttributeValueFound = mapVersionAttr.get(this.versionAttributeName);

		if (versionAttributeValueFound == null) {
			return false;
		}

		return versionAttributeValueFound.equals(this.versionAttributeValue);
	}

	@Override
	public boolean canMatchChildren(ReferencePath referencePath) {
		return true;
	}
}
