# JSON访问
在NyaPlus中，我们内置JSON的解析的语法。

## 创建数组
NyaPlus中，JSON是按照字符串的方式存储的，因此我们可以使用字符串的方式创建数组。
```NyaPlus
a:[1,2,3,"nya","abc"]
b:{"name":"neko","age":18,"item":["apple","banana","orange"]}
```
## 读取数组元素
在访问单字符名称的数组时，我们可以使用以下语法访问。
多层数组（对象）访问时，可以连续缀以括号连续访问
```NyaPlus
A:{"name":"neko","age":18,"item":["apple","banana","orange"]}
@A[name]  -->  neko
@A[item][1]  -->  banana
```
至于JSON的修改，删除等，需要依赖函数来实现了。
