/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2015 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package javax.mvc;

import javax.mvc.annotation.UriRef;
import javax.mvc.security.Csrf;
import javax.mvc.security.Encoders;
import javax.ws.rs.core.Configuration;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * <p>This class provides contextual information such as context and application
 * paths as well as access to the JAX-RS application configuration object.
 * In addition, it provides access to the security-related beans {@link
 * javax.mvc.security.Csrf} and {@link javax.mvc.security.Encoders}.</p>
 *
 * <p>Implementations of this class are injectable, must be
 * {@link javax.enterprise.context.RequestScoped} and accessible from EL using
 * the name {@code mvc}. For example, the CSRF token name and value can be
 * accessed in EL using the expressions {@code mvc.csrf.name} and {@code
 * mvc.csrf.token}, respectively.</p>
 *
 * @author Santiago Pericas-Geertsen
 * @see javax.ws.rs.core.Configuration
 * @since 1.0
 */
public interface MvcContext {

    /**
     * Get the JAX-RS application configuration object. All application-defined properties
     * are accessible via this object.
     *
     * @return the configuration object.
     */
    Configuration getConfig();

    /**
     * <p>Get the application's context path. The value returned must be normalized
     * to start with a slash ({@code '/'}) but not end with one. Thus, the path returned by
     * this method can be prepended to that returned by {@link #getApplicationPath()}.</p>
     *
     * <p>For example, given the URI {@code http://host:port/myapp/resources/hello},
     * this method returns {@code /myapp}.</p>
     *
     * @see javax.servlet.ServletContext#getContextPath()
     * @return the application's context path.
     */
    String getContextPath();

    /**
     * <p>Get the application's path which was set using the {@link javax.ws.rs.ApplicationPath}
     * annotation. The value returned must be normalized to start with a slash ({@code '/'}) but
     * not end with one. Thus, the path returned by this method can be appended to that
     * returned by {@link #getContextPath()}.</p>
     *
     * <p>If the application path is empty or was set to {@code /*}, then an empty string is
     * returned to ensure concatenation with {@link #getContextPath()} results in a well-formed
     * path. If a JAX-RS application subclass is not found, {@code null} may be returned.</p>
     *
     * <p>For example, given the URI {@code http://host:port/myapp/resources/hello},
     * this method returns {@code /resources}.</p>
     *
     * @return the application's path or {@code null} if not found.
     */
    String getApplicationPath();

    /**
     * <p>Get the application's base path which is defined as the concatenation of context
     * and application paths. It follows that the value returned by this method always
     * starts with a slash but never ends with one.</p>
     *
     * <p>If a JAX-RS application subclass is not found, causing {@link #getApplicationPath()}
     * to return {@code null}, then the value returned is the same as {@link #getContextPath}.</p>
     *
     * @return the application's base path.
     */
    String getBasePath();

    /**
     * Get the CSRF object.
     *
     * @return the CSRF object.
     */
    Csrf getCsrf();

    /**
     * Get the built-in encoders.
     *
     * @return instance of encoders.
     */
    Encoders getEncoders();

    /**
     * Returns the locale of the current request.
     *
     * @return The request locale
     */
    Locale getLocale();

    /**
     * <p>Creates an URI to be matched by a controller method. This is aimed primarily
     * for use in view rendering technologies to avoid duplicating the values of the
     * {@link javax.ws.rs.Path} annotations.</p>
     *
     * <p>The controller method can either be identified by the simple name of the controller class
     * and the method name separated by '#' (MyController#myMethod) <em>or</em> by the value
     * of the {@link UriRef} annotation.</p>
     *
     * <p>The created URI includes context- and application path.</p>
     *
     * <p>This method assumes that there is no parameter in the URI-template.</p>
     *
     * <p>For example:</p>
     * <pre><code>${mvc.uri('MyController#myMethod')}</code></pre>
     *
     * @param identifier for the controller method.
     * @return the constructed URI including context- and application path.
     */
    URI uri(String identifier);

    /**
     * <p>Creates an URI to be matched by a controller method. This is aimed primarily
     * for use in view rendering technologies to avoid duplicating the values of the
     * {@link javax.ws.rs.Path} annotations.</p>
     *
     * <p>The controller method can either be identified by the simple name of the controller class
     * and the method name separated by '#' (MyController#myMethod) <em>or</em> by the value
     * of the {@link UriRef} annotation.</p>
     *
     * <p>The created URI includes context- and application path.</p>
     *
     * <p>Any {@link javax.ws.rs.PathParam} parameter found in the URI-template
     * will be replaced by the values of the supplied List in the same order as provided.
     * Note that query parameters and matrix parameters are not supported
     * by this method as their order is not predefined.</p>
     *
     * <p>For example:</p>
     * <pre><code>${mvc.uri('MyController#myMethod', ['foo', 42])}</code></pre>
     *
     * @param identifier for the controller method.
     * @param params a list of path parameters.
     * @return the constructed URI including context- and application path.
     * @throws IllegalArgumentException if there are any URI template parameters without a supplied value, or if a value is {@code null}.
     */
    URI uri(String identifier, List<Object> params);

    /**
     * <p>Creates an URI to be matched by a controller method. This is aimed primarily
     * for use in view rendering technologies to avoid duplicating the values of the
     * {@link javax.ws.rs.Path} annotations.</p>
     *
     * <p>The controller method can either be identified by the simple name of the controller class
     * and the method name separated by '#' (MyController#myMethod) <em>or</em> by the value
     * of the {@link UriRef} annotation.</p>
     *
     * <p>The created URI includes context- and application path.</p>
     *
     * <p>Any {@link javax.ws.rs.PathParam}, {@link javax.ws.rs.QueryParam}
     * and {@link javax.ws.rs.MatrixParam} which could apply for given target
     * method will be replaced if a matching key is found in the supplied Map.
     * Please note that the map must contain values for all path parameters
     * as they are required for building the URI. All other parameters are optional.</p>
     * 
     * <p>For example:</p>
     * <pre><code>${mvc.uri('MyController#myMethod' {'foo': 'bar', 'id': 42})}</code></pre>
     *
     * @param identifier for the controller method.
     * @param params a map of path-, query- and matrix parameters.
     * @return the constructed URI including context- and application path.
     * @throws IllegalArgumentException if there are any URI template parameters without a supplied value, or if a value is {@code null}.
     */
    URI uri(String identifier, Map<String, Object> params);

    /**
     * <p>Returns a {@link MvcUriBuilder} for building URIs to be matched
     * by a controller method. This is aimed primarily for use in Java classes.</p>
     *
     * <p>The controller method can either be identified by the simple name of the controller class
     * and the method name separated by '#' (MyController#myMethod) <em>or</em> by the value
     * of the {@link UriRef} annotation.</p>
     *
     * @param identifier for the controller method.
     * @return a reference to a {@link MvcUriBuilder}.
     */
    MvcUriBuilder uriBuilder(String identifier);

}
