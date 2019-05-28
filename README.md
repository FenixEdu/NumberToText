# NumberToText


## Overview

Java lib for converting a number to the corresponding text notation.

The current version only supports Portgugues. The PT version is largly based on stuff we found on the internet.

Contributions for other languages are welcome.


## Building

```bash
mvn install
```

## Using

First, add the dependency in your project, module or application.
If you use maven, add the following dependency, adjusting the version adequately:

```xml
   <dependency>
      <groupId>org.fenixedu</groupId>
      <artifactId>numberToText</artifactId>
      <version>${version.org.fenixedu.numberToText}</version>
   </dependency>
```

This artifact is available in the following maven repository:

```xml
    <repositories>
        <repository>
            <id>fenixedu-maven-repository</id>
            <url>https://repo.fenixedu.org/fenixedu-maven-repository</url>
        </repository>
    </repositories>
```

The org.fenixedu.NumberToText provides a static method for writing numbers in text format.
The method currently only provides language Portugues and the following currencies: EURO, REAL, METICAL, KWANZA, DOLLAR and POUND.


## Examples

```java
org.fenixedu.NumberToText.toText(new Locale("pt"), new BigDecimal("1.23"));
org.fenixedu.NumberToText.toText(new Locale("pt", "PT"), new BigDecimal("12.23"));
org.fenixedu.NumberToText.toText(new Locale("pt", "BR"), new BigDecimal("12.23"));
org.fenixedu.NumberToText.toText(new Locale("pt", "MZ"), new BigDecimal("12.23"));
org.fenixedu.NumberToText.toText(new Locale("pt", "AO"), new BigDecimal("12.23"));
org.fenixedu.NumberToText.toText(new Locale("pt", "US"), new BigDecimal("12.23"));
org.fenixedu.NumberToText.toText(new Locale("pt", "UK"), new BigDecimal("12.23"));
```
