# JS Sorters
Implemented sorters:
- Line Sorter
- JS Import Sorter

# How to Install
Search **JSSorter** from the Indeed respository.

## Line Sorter
Sort selected lines alphabetically.

###### How to Use It
Select the lines you want to sort, you will find a new **Sort Selected Lines** option under the **Code** menu. Or hit *Shift+Alt+N*.


## JS Import Sorter
Sort JS imports. See [ESlint sort-imports](http://eslint.org/docs/rules/sort-imports)

###### How to Use It
When you're editing a js file, you will find a new **Sort JS Import** option under the **Code** menu. Or hit *Shift+Alt+J*.

###### When Not To Use It
>This rule is a formatting preference and not following it won’t negatively affect the quality of your code. If alphabetizing imports isn’t a part of your coding standards, then you can leave this rule disabled. 

:yum: I don't know **require**s, **import**s only.

:smiling_imp: I will **break** your code if you use multi-line imports.
```JavaScript
import {
AppRegistry, 
StyleSheet, 
Text } from 'react-native';
```


