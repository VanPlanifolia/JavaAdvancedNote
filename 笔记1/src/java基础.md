## Java基础

##### 1.String与StringBuffer有哪些区别

* 首先，用一句话概括一下他们的不同点：它们就是一个变量和常量的关系。StringBuffer对象的内容可以修改，就想当与我们通常所说的变量了；而String对象一旦产生后就不可以被修改，重新赋值会产生一个新的对象，因而String对象就相当于我们所说的常量了。
* StringBuffer的方法和String不同，StringBuffer在进行字符串处理时，不生成新的对象，在内存使用上要优于String类。所以在实际使用时，如果经常需要对一个字符串进行修改，例如插入、删除等操作，使用StringBuffer要更加适合一些。反之，用String比较安全。

##### 2.Array和ArrayList的区别

* 首先Array是数组而ArrayList是列表，我们在创建Array的时候要预先声明Array的长度，一旦长度确定了之后就不可变了，ArrayList的长度是可变的在每次添加时，如果发现空间不足的话，会创建一个长度大概是原来1.5倍的新数组，然后把原来的数组元素复制过去。
* Array作为数组可以存放基本数据类型和对象数据，而ArrayList只能存放对象数据类型，如果想要存放基本数据类型的话可以使用他们的包装类
* Array在存取值的时候只能够通过下标来进行存取，而ArrayList提供了一系列的方法比如add，remove等等
* 如果使用过程中长度不会变则推荐使用数组，如果长度不确定，可能会动态变化则推荐使用列表

##### 3. 值传递与引用传递的区别

* 值传递，是指在调用函数时将实际参数 **复制** 一份传递到函数中，这样在函数中如果对 参数 进行修改，将不会影响到实际参数。传递对象往往为整型浮点型字符型等基本数据结构。
* 引用传递，是指在调用函数时将实际参数的**地址** 直接 传递到函数中，那么在函数中对 参数 所进行的修改，将影响到实际参数。（类似于共同体） 传递对象往往为数组等地址数据结构。

##### 3.object不重写hashcode的话，hashcode如何生成的

* Object的hashCode方法是本地方法，也就是说是使用C或者C++语言实现的

##### 4.我们重写了equals为什么一定要重写hashcode

* 因为我们进行对象比较先使用hashcode进行比较可以提高效率，所以我们若是重写了equals方法，而不去重写hashcode方法这样会导致，通过equals比较这两个对象的属性是一致的返回true而由于我们没有重写hashcode，而在hashcode判断这两个对象的哈希值时发现不一致而返回false去导致最终结果为false。

##### 5.如何通过反射来获取修改类的私有属性和

// 1. 获取class对象

Class clazz = Person.``class``;

// 2. 获取私有无参构造`

Constructor c = clazz.getDeclaredConstructor();

// 3. 设置访问为可见

c.setAccessible(true);

4. 通过构造器创建实例对象

Person person = (Person) c.newInstance();

// 根据字段名称获取class中的字段`

Field age = clazz.getDeclaredField(``"age"``);

age.setAccessible(``true``);`

// 修改私有变量的默认值`

age.set(person, ``18``);`
