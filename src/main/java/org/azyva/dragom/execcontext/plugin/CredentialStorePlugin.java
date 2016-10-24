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

package org.azyva.dragom.execcontext.plugin;

/**
 * Provides access to credentials.
 * <p>
 * Used by classes that need to pass credentials to external systems such as
 * Jenkins.
 * <p>
 * Provides only read access to credentials. This interface is not meant to allow
 * managing the credentials (adding, updating, removing) as it is not expected
 * that callers within Dragom need to manage credentials. However, nothing prevents
 * an implementation class to provide such facilities that a corresponding tool
 * class can use. {@link DefaultCredentialStoarePluginImpl} does, and
 * {@link CredentialManagerTool} is a CLI tool that allows the user to manage
 * credentials.
 * <p>
 * If requested credentials do not exist, the implementation class can interact
 * with the user (through {@link UserInteractionCallbackPlugin}) to obtain the
 * missing credentials if {@link UserInteractionCallbackPlugin#isBatchMode}
 * returns false.
 * <p>
 * This interface supports the concept of resource to which credentials are
 * associated. The meaning of resources is not defined by this interface. But it
 * is expected that resources be URL of external systems. It is the responsibility
 * of the implementing class to implement the mapping logic between credentials
 * and resources. Generally such mapping is not one to one since there may be
 * multiple resources requiring the same credentials. In such cases, the
 * implementation class can classify resources by realms and map credentials to
 * realms. But this is purely an implementation detail.
 * <p>
 * Only password-type credentials are supported, or credentials that can be
 * stored as a simple String.
 *
 * @author David Raymond
 */
public interface CredentialStorePlugin extends ExecContextPlugin {
	/**
	 * Validates credentials.
	 * <p>
	 * The caller can implement this interface to allow the implementation (of
	 * CredentialStorePlugin) to validate the credentials before returning them. This
	 * is particularly useful if the implementation interacts with the user to obtain
	 * missing credentials which may be entered incorrectly.
	 */
	public interface CredentialValidator {
		/**
		 * Indicates if the credentials are valid for the given resource.
		 *
		 * @param resource Resource.
		 * @param user User.
		 * @param password Password.
		 * @return See description.
		 */
		boolean validateCredentials(String resource, String user, String password);
	}

	/**
	 * Holds credentials.
	 */
	public class Credentials {
		/**
		 * Resource for which the credentials apply.
		 */
		public String resource;

		/**
		 * User.
		 */
		public String user;

		/**
		 * Password.
		 */
		public String password;

		/**
		 * Indicates if the user is specific to the resource, embedded in the resource, as
		 * is often the case in URL (e.g., {@code https://<user>@<server>/...}).
		 */
		public boolean indUserSpecificResource;
	}

	/**
	 * Indicates if the credentials are available for the specified resource and user.
	 * <p>
	 * If user is null it means the caller does not know the user and expects the
	 * CredentialStorePlugin to provide it. It is up to the implementation to support
	 * extracting the user from the resource (e.g.,
	 * {@code https://<user>@<server>/...}) and/or mapping default users to resources.
	 * <p>
	 * If user is not null and the implementation supports extracting the user from
	 * the resource, it should validate that the user is the same as that specified
	 * by the resource, if any. If they do not match, an exception should be raised
	 * since if the caller specified the user it presumably comes from some
	 * configuration and is not designed to change it.
	 * <p>
	 * If there is no credentials stored for the specified resource and user, and the
	 * implementation supports interacting with the user to obtain missing
	 * credentials, this method should perform that interaction since although not
	 * stored, credentials are conceptually available since they can be obtained from
	 * the user. If true is returned, {@link #getCredentials} is expected to be called
	 * next to get them which should not cause any interaction since already performed
	 * by this method.
	 * <p>
	 * If credentialValidator is not null, the method should validate the credentials
	 * before returning them. This includes both the cases where the credentials are
	 * already available (since they may be invalid) and when the method interacts with
	 * the user to obtain them.
	 *
	 * @param resource Resource.
	 * @param user User. Can be null (see above).
	 * @param credentialValidator CredentialValidator. Can be null (see above).
	 * @return See description.
	 */
	boolean isCredentialsExist(String resource, String user, CredentialValidator credentialValidator);

	/**
	 * Returns the credentials for the specified resource and user. If requested
	 * credentials are not available (even following interaction with the user, if
	 * appropriate), an exception should be raised. If the caller handles unavailable
	 * credentials, {@link #isCredentialsExist} should be used before calling this method.
	 * <p>
	 * If user is null it means the caller does not know the user and expects the
	 * CredentialStorePlugin to provide it. It is up to the implementation to support
	 * extracting the user from the resource (e.g.,
	 * {@code https://<user>@<server>/...}) and/or mapping default users to resources.
	 * <p>
	 * If user is not null and the implementation supports extracting the user from
	 * the resource, it should validate that the user is the same as that specified
	 * by the resource, if any. If they do not match, an exception should be raised
	 * since if the caller specified the user it presumably comes from some
	 * configuration and is not designed to change it.
	 * <p>
	 * If there is no credentials stored for the specified resource and user, the
	 * implementation can interact with the user to obtain the missing credentials.
	 * <p>
	 * If credentialValidator is not null, the method should validate the credentials
	 * before returning them. This includes both the cases where the credentials are
	 * already available (since they may be invalid) and when the method interacts with
	 * the user to obtain them.
	 *
	 * @param resource Resource.
	 * @param user User. Can be null (see above).
	 * @param credentialValidator CredentialValidator. Can be null (see above).
	 * @return Credentials. Cannot be null.
	 */
	Credentials getCredentials(String resource, String user, CredentialValidator credentialValidator);

	/**
	 * Resets the credentials for the specified resource and user.
	 * <p>
	 * This method allows the caller to implement credential validation logic without
	 * using {@link CredentialStorePlugin.CredentialValidator}. The caller would call
	 * {@link #getCredentials} (which could cause interaction with the user) and
	 * validate the credentials. If invalid, the caller would call this method and
	 * loop.
	 * <p>
	 * If user is null it means the caller does not know the user and expects the
	 * CredentialStorePlugin to provide it. It is up to the implementation to support
	 * extracting the user from the resource (e.g.,
	 * {@code https://<user>@<server>/...}) and/or mapping default users to resources.
	 * If no user can be determined, the method should do nothing. It should not
	 * attempt to interact with the user.
	 * <p>
	 * If user is not null and the implementation supports extracting the user from
	 * the resource, it should validate that the user is the same as that specified
	 * by the resource, if any. If they do not match, an exception should be raised
	 * since if the caller specified the user it presumably comes from some
	 * configuration and is not designed to change it.
	 * <p>
	 *
	 * @param resource Resource.
	 * @param user User. Can be null (see above).
	 */
	void resetCredentials(String resource, String user);
}
