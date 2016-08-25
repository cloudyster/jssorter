# Sorters for editing javascript file
Implemented sorters:
- Lines sorter
- Switch cases sorter
- Javascript imports sorter

# How to Install
Download the [jar](https://github.com/loevaljh/jssorter/blob/master/JSSorter.jar) and In Intellij choose *Install plugin from disk*.

## Lines sorter
Sort selected lines alphabetically.

###### How to Use It
Select the lines you want to sort, then you will find a new **Sort Selected Lines** option under the **Code** menu. Or hit *Shift+Alt+N*.

## Switch cases sorter
Sort switch cases alphabetically.

###### How to Use It
Place the cursor under the _switch_ word, then you will find a new **Sort Switch Cases** under the **Code** menu. Or hit *Shift+Alt+S*.

## Javascript imports sorter
Sort javascript imports. See [ESlint sort-imports](http://eslint.org/docs/rules/sort-imports)

###### How to Use It
When you're editing a js file, you will find a new **Sort JS Import** option under the **Code** menu. Or hit *Shift+Alt+J*.

###### When Not To Use It
:smiling_imp: You use **require**, but not **import**.

:smiling_imp: You have multi-line imports.
```JavaScript
import {
AppRegistry, 
StyleSheet, 
Text } from 'react-native';
```
:smiling_imp: Alphabetizing imports isn’t a part of your coding standards.




