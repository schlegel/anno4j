package com.github.anno4j.rdf_generation.generation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.anno4j.rdf_generation.configuration.Configuration;
import com.github.anno4j.rdf_generation.building.Extractor;
import com.google.common.reflect.ClassPath;

public class RdfFileGenerator implements FileGenerator {

	/**
	 * The content of the RDFS file which will be stored represented as a string.
	 */
	private String content;

	/**
	 * The configuration settings for generating a RDFS file.
	 */
	private Configuration config;
	
	/**
	 * The path where the package or class to convert is stored. Needed to analyse
	 * if more than one class should be converted.
	 */
	private String packages;
	
	private boolean isPackage;

	/**
	 * The list where all classes which will be converted are stored.
	 */
	private List<Class<?>> allclasses = new ArrayList<>();

	/**
	 * The constructor of the FileGenerator.
	 * 
	 * @param config   The configurations needed for the convertion.
	 * @param packages The path to the converted package or class, needed for
	 *                 analysis.
	 */
	public RdfFileGenerator(Configuration config, String packages) {
		content = "";
		this.config = config;
		this.packages = packages;
	}

	/**
	 * All classes to be converted are being stored in the allclasses list. If the
	 * boolean "bundled" is false, every class contained in the list will be
	 * converted separately. If true, one bundled file which contains all classes
	 * contained in the list will be generated.
	 */
	@Override
	public void generate() throws IOException {
		allclasses = loadAllClasses();
		if (!isPackage) {
			for (Class<?> clazz : allclasses) { // nur für !bundled
//				System.out.println("Size of my Classes-List :" + allclasses.size());
				generateFile(clazz); // jede file wird extra generiert.
				
			}
		} else {
			generateBundledFile(allclasses); // einzige file für alle klassen
		}
	}

	/**
	 * Loads all classes from the path of a package or class. If the name of the
	 * package doesn't end with a '.' and matches the name of a class perfectly,
	 * only this one class will be stored in the list. If the name of the package
	 * ends with a '.', it is assumed that the user wants to convert all classes
	 * contained in the given package and all of them are stored in the list.
	 * 
	 * @return The list of all classes which should be converted to a RDFS file.
	 * @throws IOException
	 */
	private List<Class<?>> loadAllClasses() throws IOException {
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();
		// Start reader by specifying for example how the name of the package "starts
		// with"
		for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {

			if (info.getName().matches(packages) && !packages.endsWith(".")) {
				final Class<?> clazz = info.load();
				allclasses.add(clazz);
//				System.out.println(clazz.getCanonicalName());
				if(allclasses.size() < 2) {
					setPackage(false);
				}
				return allclasses; // lädt nur eine klasse, da packagestruktur nicht mit punkt endete und ein pfad
									// perfekt mit der eingabe übereinstimmt
			} else if (info.getName().startsWith(packages) && packages.endsWith(".")) {
				final Class<?> clazz = info.load();
				allclasses.add(clazz); // lädt alle klassen in dem package
			}

		}
		if(allclasses.size() > 1) {
			setPackage(true);
		}
		return allclasses;
	}

	/**
	 * Generates one output file from contaning all classes stored in allclasses, if
	 * necessary converts the file into the required serialization and stores the
	 * RDFS file.
	 * 
	 * @param allclasses The list where all classes to be converted are stored.
	 * @throws IOException
	 */
	private void generateBundledFile(List<Class<?>> allclasses) throws IOException {
		content = Extractor.extractMany(allclasses);
		serialCheckAndWrite(content);
	}

	/**
	 * Generates one output file for the given class, if necessary converts the file
	 * into the required serialization and stores the generated RDFS file.
	 * 
	 * @param clazz The class which will be converted.
	 * @throws IOException
	 */
	private void generateFile(Class<?> clazz) throws IOException { // generiert eine
		content = Extractor.extractOne(clazz);
//		Extractor.clear();
		serialCheckAndWrite(content);
	}

	/**
	 * Checks if the generated RDFS file is already in the correct serialization. If
	 * not it is converted into the correct serialization. Afterwards the file is
	 * being stored.
	 * 
	 * @param content The already converted RDFS file in "RDF/XML".
	 * @throws IOException
	 */
	private void serialCheckAndWrite(String content) throws IOException {
		if (config.getSerialization() == "RDF/XML") {
			writeFile(content, config.getOutputPath()); // delete "", only to avoid NullPointer since generating class
			// doesn't work
		} else if (config.getSerialization() == "TURTLE") {
			// Converter
		} else if (config.getSerialization() == "N3") {
			// Converter
		} else {
			System.out.println("WRONG SERIALIZATION");
		}

	}

	/**
	 * Stores the generated RDFS file.
	 * 
	 * @param content The converted RDFS file.
	 * @param path    The path where to store the file.
	 * @throws IOException
	 */
	public void writeFile(String content, String path) throws IOException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		writer.write(content);
		writer.close();
	}

	public boolean isPackage() {
		return isPackage;
	}

	public void setPackage(boolean isPackage) {
		this.isPackage = isPackage;
	}
}
