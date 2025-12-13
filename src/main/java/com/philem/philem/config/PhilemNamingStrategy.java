package com.philem.philem.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class PhilemNamingStrategy implements PhysicalNamingStrategy {

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        // IMPORTANT: keep table names EXACTLY as declared/logical name
        return name;
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        if (name == null) return null;
        String text = name.getText();
        String snake = toSnakeCase(text);
        return Identifier.toIdentifier(snake, name.isQuoted());
    }

    // The rest can be pass-through (defaults) or same as column behavior as you prefer.
    @Override public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment context) { return name; }
    @Override public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context) { return name; }
    @Override public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) { return name; }

    private String toSnakeCase(String input) {
        // camelCase / PascalCase -> snake_case
        // e.g. soldYear -> sold_year, URLValue -> url_value
        String s = input
                .replaceAll("([a-z0-9])([A-Z])", "$1_$2")
                .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
                .toLowerCase();
        return s;
    }
}
