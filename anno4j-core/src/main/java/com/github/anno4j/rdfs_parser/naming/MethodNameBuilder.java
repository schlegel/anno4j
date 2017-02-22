package com.github.anno4j.rdfs_parser.naming;

import com.squareup.javapoet.MethodSpec;
import org.openrdf.query.algebra.Str;

import java.net.URISyntaxException;

/**
 * Generates Java compliant method names for resources.
 */
public class MethodNameBuilder extends IdentifierBuilder {

    /**
     * @param resource The resource to build a name for.
     */
    protected MethodNameBuilder(String resource) {
        super(resource);
    }

    /**
     * Creates a new MethodNameBuilder object for the given resource.
     * @param resource The resource to build a name for.
     * @return A builder instance.
     */
    public static MethodNameBuilder builder(String resource) {
        return new MethodNameBuilder(resource);
    }

    /**
     * Generates a JavaPoet method spec for an empty getter representing the property resource.
     * The method is named according to Java naming conventions with preceeding "get".
     * @return JavaPoet method object. Can be modified by calling
     * {@link MethodSpec#toBuilder()} on it.
     * @throws URISyntaxException If the URI violates RFC 2396 augmented by the rules defined in URI and the
     * requirement for a hostname component.
     * @throws NameBuildingException If the required information for building the name is not contained in the URI.
     */
    public MethodSpec getterSpec() throws URISyntaxException, NameBuildingException {
        return MethodSpec.methodBuilder("get" + capitalizedIdentifier()).build();
    }

    /**
     * Generates a JavaPoet method spec for an empty getter representing the property resource.
     * The method is named according to Java naming conventions with preceeding "get" and a trailing
     * "s" to indicate that this method returns more than one value.
     * @return JavaPoet method object. Can be modified by calling
     * {@link MethodSpec#toBuilder()} on it.
     * @throws URISyntaxException If the URI violates RFC 2396 augmented by the rules defined in URI and the
     * requirement for a hostname component.
     * @throws NameBuildingException If the required information for building the name is not contained in the URI.
     */
    public MethodSpec getterSpecPlural() throws URISyntaxException, NameBuildingException {
        StringBuilder identifier = new StringBuilder("get");
        identifier.append(capitalizedIdentifier());

        // Replace trailing "y" with "ie". E.g. "capacity" will be transformed to "capacities":
        if(identifier.charAt(identifier.length() - 1) == 'y') {
            identifier.deleteCharAt(identifier.length() - 1);
            identifier.append("ie");
        }

        // Only append trailing "s" if the name does not yet end with one:
        if(identifier.charAt(identifier.length() - 1) != 's') {
            identifier.append("s");
        }
        return MethodSpec.methodBuilder(identifier.toString()).build();
    }

    /**
     * Generates a JavaPoet method spec for an empty setter representing the property resource.
     * The method is named according to Java naming conventions with preceeding "set".
     * @return JavaPoet method object. Can be modified by calling
     * {@link MethodSpec#toBuilder()} on it.
     * @throws URISyntaxException If the URI violates RFC 2396 augmented by the rules defined in URI and the
     * requirement for a hostname component.
     * @throws NameBuildingException If the required information for building the name is not contained in the URI.
     */
    public MethodSpec setterSpec() throws URISyntaxException, NameBuildingException {
        return MethodSpec.methodBuilder("set" + capitalizedIdentifier()).build();
    }

    /**
     * Generates a JavaPoet method spec for an empty setter representing the property resource.
     * The method is named according to Java naming conventions with preceeding "set" and a trailing
     * "s" to indicate that this method returns more than one value.
     * @return JavaPoet method object. Can be modified by calling
     * {@link MethodSpec#toBuilder()} on it.
     * @throws URISyntaxException If the URI violates RFC 2396 augmented by the rules defined in URI and the
     * requirement for a hostname component.
     * @throws NameBuildingException If the required information for building the name is not contained in the URI.
     */
    public MethodSpec setterSpecPlural() throws URISyntaxException, NameBuildingException {
        StringBuilder identifier = new StringBuilder("set");
        identifier.append(capitalizedIdentifier());

        // Replace trailing "y" with "ie". E.g. "capacity" will be transformed to "capacities":
        if(identifier.charAt(identifier.length() - 1) == 'y') {
            identifier.deleteCharAt(identifier.length() - 1);
            identifier.append("ie");
        }

        // Only append trailing "s" if the name does not yet end with one:
        if(identifier.charAt(identifier.length() - 1) != 's') {
            identifier.append("s");
        }
        return MethodSpec.methodBuilder(identifier.toString()).build();
    }

    /**
     * Generates a JavaPoet method spec for an empty add method representing the property resource.
     * The method is named with preceeding "set".
     * @return JavaPoet method object. Can be modified by calling
     * {@link MethodSpec#toBuilder()} on it.
     * @throws URISyntaxException If the URI violates RFC 2396 augmented by the rules defined in URI and the
     * requirement for a hostname component.
     * @throws NameBuildingException If the required information for building the name is not contained in the URI.
     */
    public MethodSpec adderSpec() throws URISyntaxException, NameBuildingException {
        return MethodSpec.methodBuilder("add" + capitalizedIdentifier()).build();
    }

    @Override
    public MethodNameBuilder withRDFSLabel(String label) {
        super.withRDFSLabel(label);
        return this;
    }
}
