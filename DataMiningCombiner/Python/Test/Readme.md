# Operation notation
```yaml
operation:
  id: "6296f97c-c7c1-49fe-b50d-38e555705ee8"
  name: operationName
  type: operationType
  arguments:
    - name: firstArgument
      type: String
    - name: secondArgument
      type: Int
  operations:
    - id: "d16e727c-b56b-45cf-ae0f-f18bd839acbf"
      name: firstOperation
    - id: "e9265941-679d-447f-8974-259406b524fb"
      name: secondOperation
  results:
    - name: resultOfOperation
      type: String
```

- Entry point of each operation is function ```entry```
- Operation has uniq identifier and name
- Operation has his own type e.g. python... or smth else
- Operation has arguments. Each argument is presented by name and type
- Complicated operations have nested operations which described in correspondent field ```operations```
- Returning values described in field ```results```. If operation returns nothing we can scip this field

### Types of arguments
- Int or Int[]
- Float or Float[]
- String or String[]
- Boolean or Boolean[]


### Open questions
- What we should do with complicated objects in arguments and returning values
