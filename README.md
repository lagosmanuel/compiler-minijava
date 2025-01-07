# Mini-Java Compiler

This project implements a compiler for the _Mini-Java programming language_, a
subset of Java designed for educational purposes. The compiler generates code
for a virtual machine developed by the Compilers and Interpreters course at
the _Department of Computer Science and Engineering_, _National University of
the South_. Visit the [website of the Department](https://cs.uns.edu.ar).

Mini-Java supports object-oriented programming, allowing the definition of
classes, methods, constructors, and the use of attributes and local variables
with basic and user-defined types, as well as a simple structure for managing
inheritance and performing basic operations on objects.

## Objective

The main goal of this project is to provide a tool that translates source code
written in Mini-Java into intermediate code that can be executed on a custom
virtual machine. This project is part of the Compilers and Interpreters
course, where fundamental concepts for the development of compilers and
programming languages are explored.

## Project Structure

The compiler is divided into several stages that correspond to the classic
phases of a compiler. The main stages are as follows:

1. **Lexical Analysis**: This stage reads the source code and converts it into
   a sequence of tokens representing the basic elements of the language (such
   as keywords, identifiers, operators, etc.).

2. **Syntactic Analysis**: In this stage, the compiler constructs an abstract
   syntax tree (AST) from the token sequence. The syntax rules of the Mini-Java
   language are validated here.

3. **Semantic Analysis**: During this phase, the code is checked for semantic
   correctness, ensuring that expressions, data types and declarations are
   valid (for example, checking that variables are used after being declared).

4. **Code Generation**: Finally, the intermediate code is converted into
   specific instructions for the virtual machine, ready to be executed.

## Supported Features

The compiler supports the following language features:

- **Class Declaration**: Classes with attributes and methods can be declared.
- **Methods**: Support for methods with parameters and return types, both
  static and non-static.
- **Method Overloading**: It is possible to define multiple methods with the
  same name but a different number of parameters.
- **Inheritance**: Basic support for class inheritance.
- **Abstract Classes**: Support for abstract classes.
- **Primitive Types**: Support for types such as `int`, `char`, `boolean` and
  `String`.
- **Basic Operators**: Support for arithmetic, logical and comparison
  operators.
- **Wrapper Classes**: Support for wrapper classes `Integer`, `Character` and
  `Boolean`.
- **Control Flow**: `if`, `if-else`, `switch` statements; and `while`, `for`
  and `foreach` loops.
- **Attribute Hiding**: Support for hiding inherited attributes. 
- **Super Access**: Support for accessing the superclass constructors, methods
  and attributes.
- **Standard Library Methods**: The compiler provides a standard library of
  classes, such as `System` with methods for output and input handling.

## Unsupported Features

The following features are currently not supported:

- **Operator Precedence**: All operators have the same precedence and are left
  associative.
- **Class Visibility**: No support for class visibility, but visibility in
  methods and attributes is supported.
- **Exceptions**: No support for exception handling (`try-catch`).
- **Interfaces**: Support for interfaces is not yet available.
- **Generics**: No support for generic classes or methods has been implemented.
- **Arrays**: No support for arrays.
- **Pre-Increment and Post-Increment Operators**: No support for pre-increment (e.g. ++i) and post-decrement
  (e.g. i++) operators has been implemented, but post-increment can be replaced with the augmented
  assignment operator `+=` (e.g. i+=1). Idem with the decrement version.
- **Float Type**: No support for the `Float` type.

## Grammar

The LL(1) Context-Free Grammar of the language's syntax can be found in the file 
[grammar.txt](resources/grammar/syntax-grammar.txt).

## Prerequisites
- Java Development Kit version 21 or higher

## Compile the Project
   1. Clone the Project.
   ```bash
   git clone https://github.com/lagosmanuel/mini-java.git
   cd mini-java
   ```
   2. Compile the Project.
   ```bash
   javac -sourcepath src -d out src/main/java/main/Main.java
   ```

## Example
   1. Compile the [example program](resources/examples/example.java).
   ```bash
   java -cp out main.java.main.Main resources/examples/example.java example.out
   ```
   2. Run the output on the Virtual Machine
   ```bash
   java -jar VM.jar example.out
   ```

## License

This project is licensed under the GNU License - see the [LICENSE](LICENSE) file for details.
