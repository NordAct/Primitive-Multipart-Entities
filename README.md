# Primitive-Multipart-Entities
A small library for making multipart entities within Fabric ecosystem. Requires Fabric API

# Usage
## Adding as dependency
### Via Jitpack
Use Jitpack to add this in your project. This goes to build.gragle:
```gradle
repositories {
  maven { url 'https://jitpack.io' }
}

dependencies {
   modImplementation include ("com.github.NordAct:Primitive-Multipart-Entities:{current-branch-name}-SNAPSHOT")
}
```
Replace `{current-branch-name}` with branch you want to use. This will grab latest commit on specified branch. If you want to be more specific on which commit to grab or just wanna know how Jitpack works, visit https://jitpack.io/ for more info.

### As Local Library
1. Get release you want from GitHub releases (or download repository and compile locally if one you want isn't there)
2. Create directory in root of your project (for simplicity we'll call it `libs`) and place .jar file in it
3. Add the following to your build.gragle:
```gradle
repositories {
      flatDir(dirs: "libs") //libs is directory in root of your project from step 2
}

dependencies {
   modImplementation include ("nordmods.primitive_multipart_entities:Primitive-Multipart-Entities:{version}")
}
```
Replace `{version}` with version you working with.

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
private final EntityPart[] parts = new EntityPart[]{part1, part2, part3};
```
`EntityPart` is... entity part. It takes 3 parameters: owner, width and height. You can use it as it is or extend and change for your needs.

To change part's position relative to the owner use `setRelativePos()`. Note that you can additionally pass pitch and yaw to set it relative to some rotation.
