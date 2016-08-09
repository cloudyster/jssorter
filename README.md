# Sort JS imports
See [ESlint sort-imports](http://eslint.org/docs/rules/sort-imports)

## How to Install
Search **JSImport** from the Indeed respository.

## How to Use It
When you're editing a js file, you will find a new **Sort JS Import** option under the **Code** menu. Or hit *Shift+Alt+J*.

## When Not To Use It
>This rule is a formatting preference and not following it won’t negatively affect the quality of your code. If alphabetizing imports isn’t a part of your coding standards, then you can leave this rule disabled. 

:yum: I don't know **require**s, **import**s only.

:smiling_imp: I will **break** your code if you use multi-line imports.
```JavaScript
import {
AppRegistry, 
StyleSheet, 
Text } from 'react-native';
```