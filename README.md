# eq (edn query)

[jq][1] like tool to process and syntax higlight edn files.

## Installation

Download from https://github.com/raszi/eq.

## Usage

Run the project directly:

    $ boot run

Run the project's tests:

    $ boot test

Build an uberjar from the project:

    $ boot build

Run the uberjar:

    $ java -jar target/eq-0.1.0-SNAPSHOT.jar [options...] [filter] [files...]

## Options

 - `-C` or `--color-output` Colorize the output (this is the default)
 - `-M` or `--monochrome-output` Disable the default colored output

## Examples

Simply print out a file

    $ java -jar target/eq-0.1.0-SNAPSHOT.jar identity sample.edn

Without providing a file it will read from STDIN:

    $ cat sample.edn | java -jar target/eq-0.1.0-SNAPSHOT.jar identity

More complex example with filtering

    $ java -jar target/eq-0.1.0-SNAPSHOT.jar '(-> (get-in [:deep :nested :hash]) (inc))' sample.edn

## License

Copyright © 2016 KARASZI István.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

[1]: https://stedolan.github.io/jq/
