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

Currently, we only include  **_Strategy_** and **_Visitor_** patterns.

_More to come..._ :slightly_smiling_face:

## Overview
| Pattern               |        Kotlin (functional approach)        |
|-----------------------|:------------------------------------------:|
| [Strategy](#Strategy) |          functions + `typealias`           |
| [Visitor](#Visitor)   | pattern matching + `when` + `sealed` types |

### Strategy
All the different implementors of a strategy interface are essentially just functions.
So the context relies on a specific function definition, that at runtime, different
functions are executed.

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


