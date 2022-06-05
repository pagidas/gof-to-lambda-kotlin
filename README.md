# Gang of Four Design Patterns -- a more functional-programming approach

Inspired by [Mario Fusco's](https://twitter.com/mariofusco) attempt
([github repo](https://github.com/mariofusco/from-gof-to-lambda))
to describe the canonical implementation of design patterns from the
[Gang of Four](https://en.wikipedia.org/wiki/Design_Patterns) book to 
a more functional-programming approach.

This is my own personal take to practice getting more comfortable composing
functions when it feels more natural than the class-oriented, traditional way.

This exercise is using [Kotlin](https://kotlinlang.org/) which supports a lot
of properties from both OOP and FP paradigms.

## Theme of exercise
There are two files per pattern, one with the object-oriented approach,
and another with the functional approach. Both of the takes model 
the same problem but solve it differently. There is a simple `main`
for each file that roughly tests each approach in isolation.

Currently, we only include  **_Strategy_**, **_Visitor_** and **_Decorator_** patterns.

_More to come..._ :slightly_smiling_face:

## Overview
| Pattern                 |        Kotlin (functional approach)        |
|-------------------------|:------------------------------------------:|
| [Strategy](#Strategy)   |          functions + `typealias`           |
| [Visitor](#Visitor)     | pattern matching + `when` + `sealed` types |
| [Decorator](#Decorator) |                 functions                  |

### Strategy
We can treat a function as an `interface`, transparently swapping different implementations
as long as the signatures are the same.

Compare to Java, Kotlin offers functions as types in a very intuitive way.
You have to use the following notation:
```kotlin
(SomeType) -> SomeOtherType
```
Above is a function that reads as, accepts a parameter of type `SomeType` and returns `SomeOtherType`.
More importantly, some context that may require a function defined as above would look like:
```kotlin
fun ctx(f: (SomeType) -> SomeOtherType) {
    // implementation detail...
}
```
The above example is also referred to as **_higher order functions_**, which is about functions
that accept functions as arguments, or return other functions. Simply, it's about function composition.

Instead of relying on the generic way of defining a function type, we can use Kotlin's `typealias` feature,
to give a name to a particular function so that we know the context of this calculation.

For example:
```kotlin
typealias Discount = (Long) -> Long

fun ctx(f: Discount): Long {
    // implementation detail...
}
```
Kotlin's `typealias` feature, does not create a new type, it is equivalent to the corresponding underlying
type.

### Visitor
To apply different operations into a family of types, first we have to group them. We're doing so introducing
a sum type of the algebraic data types, which simply means that a `MyType` is either `A`, `B`, or `C`. In Java,
the closest thing to a sum type ADT is the `enum`. In Kotlin we can use the `sealed` feature which offers all
subtypes of that sealed type are known at compile time.
```kotlin
sealed interface MyType {
    object A: MyTpe
    data class B(val x: Any, val y: Any): MyType
    data class C(val z: Any): MyType
}
```

Again, all the different operations are just functions acting on the sum type (or supertype). Since, in the functional
approach we cannot use **_dynamic dispatch_** because we have decoupled behaviour from data, we must use 
**_pattern matching_** on the supertype. The closest thing of pattern matching in Kotlin is using the `when` construct 
on the sealed type.
```kotlin
fun ctx(type: MyType): Unit {
    when(type) {
        is A -> println("A")
        is B -> println("B.x=${type.x}, B.y=${type.y}")
        is C -> println("C.z=${type.z}")
    }
}
```

### Decorator
Similar to the **_Visitor_**, we can treat a function as an `interface`. Just as the canonical GoF implementation,
we define another function, wrapping the delegate, which returns the target operation but "decorated". Yet another
example of **_higher order functions_**.

With **_partial application_**, we can pass the target delegate to the wrapper function as its first argument, defining the decorated
operation beforehand. This will make things simpler, as you would use the decorated function the same way you would use
the undecorated one.

