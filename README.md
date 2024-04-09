# Primitive-Multipart-Entities
Some stuff for basic multipart entities making for Fabric.

# Usage
## Adding as dependency
Use Jitpack to add this in your project. This goes to build.gragle:
```gradle
repositories {
  maven { url 'https://jitpack.io' }
}

dependencies {
   modImplementation include ("com.github.NordAct:Primitive-Multipart-Entities:1.19.2-SNAPSHOT")
}
```
This will grab latest commit on 1.19.2 branch. If you wanna be more specific on which commit to grab or just wanna know how Jitpack works, visit https://jitpack.io/ for more info.

## Making multipart entity
To make multipart entity, you need first to make it implement `MultipartEntity` interface. Then you need to implement `getParts()` method that will return all parts this entity has as an array:
```java
public EntityPart[] getParts() {
  return parts;
}
```
Each part should be declared as class field and then be put in an array that will be returned by `getParts()`. This is an example:
```java
private final EntityPart part1 = new EntityPart(this, 1, 1);
private final EntityPart part2 = new EntityPart(this, 2, 1);
private final EntityPart part3 = new EntityPart(this, 3.2f, 1.2f);
private final EntityPart[] parts = new EntityPart[]{part1, part2, part3}
```
`EntityPart` is... entity part. It takes 3 parameters: owner, width and height. You can use it as it is or extend and change for your needs.

To change part's position relative to the owner use `setRelativePos()`. Note that you can additionally pass pitch and yaw to set it relative to some rotation.
