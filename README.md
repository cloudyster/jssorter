# JS Sorters
Implemented sorters:
- Switch Sorter
- Line Sorter
- JS Import Sorter

# How to Install
Download the [jar](https://github.com/loevaljh/jssorter/blob/master/JSSorter.jar) and In Intellij choose *Install plugin from disk*.

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

## Switch Sorter
Sort switch cases alphabetically.

###### How to Use It
Place the cursor under the _switch_ word, you will find a new **Sort Switch Cases** under the **Code** menu. Or hit *Shift+Alt+S*.
