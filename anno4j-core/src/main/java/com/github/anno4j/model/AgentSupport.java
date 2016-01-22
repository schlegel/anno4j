package com.github.anno4j.model;

import com.github.anno4j.annotations.Partial;
import java.io.ByteArrayOutputStream;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.*;

@Partial
public abstract class AgentSupport implements Agent {
    /**
     * Method returns a textual representation of the given Annotation, containing
     * its Body, Target and possible Selection, in a supported serialisation format.
     *
     * @param format The format which should be printed.
     * @return A textual representation if this object in the format.
     */
    @Override
    public String getTriples(RDFFormat format) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        RDFWriter writer = Rio.createWriter(format, out);

        try {
            this.getObjectConnection().exportStatements(this.getResource(), null, null, true, writer);

        } catch (RepositoryException | RDFHandlerException e) {
            e.printStackTrace();
        }

        return out.toString();
    }
}
