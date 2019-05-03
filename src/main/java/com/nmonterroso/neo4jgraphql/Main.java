package com.nmonterroso.neo4jgraphql;

import graphql.schema.GraphQLSchema;
import org.neo4j.graphql.SchemaBuilder;
import org.neo4j.graphql.Translator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {

  private final GraphQLSchema schema;

  private Main() throws IOException {
    String schemaDef = read("schemaDef.sdl");
    schema = SchemaBuilder.buildSchema(schemaDef, new Translator.Context());
  }

  private void run() {
    printQuery("query-friends-not-null.txt");
    printQuery("query-friends-name-notnull.txt");
    printQuery("query-empty-object.txt");
    printQuery("query-friends-some.txt");
  }

  private void printQuery(String resource) {
    try {
      Translator.Cypher translation = new Translator(schema).translate(read(resource)).get(0);
      System.out.println(String.format("%s:\n\tQUERY: %s\n\tPARAMS: %s", resource, translation.getQuery(), translation.getParams()));
    } catch (Exception e) {
      System.out.println(String.format("ERROR: %s - %s", resource, e.getMessage()));
    }
  }

  private static String read(String resource) throws IOException {
    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    File file = new File(classLoader.getResource(resource).getFile());
    return new String(Files.readAllBytes(file.toPath()));
  }

  public static void main(String[] args) throws IOException {
    new Main().run();
  }
}
