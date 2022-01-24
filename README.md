# organize-expenses

Alura Challenge Backend - Finance Log Organizer.

## Installation

If you want to play around you'll need to have Leiningen and Clojure installed.

## Usage
The standalone is on the target folder  
To generate a target folder you can run `lein uberjar`

$ java -jar organize-expenses-0.1.0-standalone.jar [args]   

Or run locally with `lein run`

## Tests

`lein test`

## Examples

### POST

Request to `http://localhost:8890/finance-record`.

Body income`
{
"description": "salary",
"value": 2500.90,
"type": "income"
}.  
`

Body expense (category is optional)`
{
"description": "iFood",
"value": 800.90,
"type": "expense",
"category": "food"
}
`

Returns`
{
"payload": {
"description": "salary",
"value": 1200.9,
"type": "income",
"id": "c56a21df-d242-45b4-aa4d-69cc40c5611a"
},
"message": "Entry saved"
}
`
### GET

Request to `http://localhost:8890/finance-records/<expense or income>`.

Request to `http://localhost:8890/finance-records/<expense or income>?description=search`.

Request to `http://localhost:8890/finance-records/<expense or income>/<year>/<month>`.

**Summary of the month** `http://localhost:8890/summary/<year>/<month>`


### PATCH

Request to `http://localhost:8890/finance-record/<id>`.  


Body income`
{
"description": "salary",
"value": 4000.90,
"type": "income"
}.  
`


Returns`
{
"payload": {
"description": "salary",
"value": 4000.9,
"type": "income",
"id": "c56a21df-d242-45b4-aa4d-69cc40c5611a"
},
"message": "Entry updated"
}
`


### DELETE

Request to `http://localhost:8890/finance-record/<id-to-be-deleted>`.


### Bugs

...

Copyright © 2022 

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
