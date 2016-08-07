# Sort JS imports
See [ESlint sort-imports](http://eslint.org/docs/rules/sort-imports)

## How to Use It
This plugin is not yet in the Indeed repository. 
If you're interested in trying it, [download the jar file](https://code.corp.indeed.com/jliu/JSImport/blob/master/JSImport.jar) and from Intellij use the **install plugin from disk** option.

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