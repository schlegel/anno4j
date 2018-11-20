package com.github.anno4j.rdf_generation;

import java.io.IOException;

import org.openrdf.repository.RepositoryException;

import com.github.anno4j.rdf_generation.configuration.Configuration;
import com.github.anno4j.rdf_generation.generation.FileGenerator;
import com.github.anno4j.rdf_generation.generation.RdfFileGenerator;

public class MainClass {

	/**
	 * Generates a RDFS File with a given serialization to a given output file from
	 * a given package or class. The user has to add the following:
	 * 
	 * - the path where package or class can be found
	 * - the path for the output file
	 * - the type of serialization
	 * - true if the user wants to generate one output
	 * file, false otherwise when the path to a package was given before
	 * 
	 * @param args
	 * @throws RepositoryException
	 * @throws FileGenerationException
	 * @throws IOException
	 */
	public static void main(String[] args) {
		String packages = "com.github.anno4j.rdf_generation.tests.";
		Configuration config = new Configuration("C:\\Users\\Brinninger Sandra\\Documents\\result.txt", "RDF/XML",
				packages);
		FileGenerator generator = new RdfFileGenerator(config, packages);
		try {
			generator.generate();
		} catch (IOException e) {
			UserMessage.showUser("IOException");
		}
	}

}