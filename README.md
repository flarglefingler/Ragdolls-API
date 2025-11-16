# Ragdolls API
A lightweight API for adding custom ragdoll physics to entities using my Ragdoll mod
https://www.curseforge.com/minecraft/mc-mods/ragdolls

This repository contains:
1. Example JSON corpse definitions (/corpses)
2. The public API code (/api)
3. Documentation on how to add your own ragdoll models
4. How to register your model class so Ragdolls can recognize it


If your mod adds custom entities—or overrides vanilla models—you can easily add ragdoll support using the API.
Example File Structure:
```
yourmod/
 └─ resources/
     └─ corpses/
         └─ your_entity.json
```
Your corpse JSON file MUST live under:
```
assets/<your_modid>/corpses/<id>.json
```
Example:
```
assets/mymod/corpses/mutant_zombie.json
```
The Ragdolls mod automatically loads all .json files found under:
*/corpses/*.json

# 2. Registering Your Entity Model
Ragdolls needs to know which EntityModel class your entity uses.
In your mod’s Client initializer, register like this:
```
import net.caoqt.ragdolls.api.RagdollModelRegistry;

public class MyModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        RagdollModelRegistry.register(
            "mymod:mutant_zombie",
            MutantZombieEntityModel.class
        );
    }
}
```

ID must match "model" in your JSON exactly!
Example JSON:
```
{
  "model": "mymod:mutant_zombie",
  ...
}
```

# 3. JSON Format Overview
Each corpse definition contains:
✔ "model"
The ID that links this JSON to your registered model.
✔ "nodes"
Physics nodes → these become RagdollLimbs.
✔ "joints"
Connections between nodes → become RagdollJoints.
✔ "parts"
Which ModelParts should stick to that limb.
Basic structure:
```
{
  "model": "mymod:mutant_zombie",

  "nodes": [
    { "name": "head", "mass": 1.5, "position": [0, 2, 0] },
    { "name": "chest", "mass": 1.0, "position": [0, 1.2, 0] }
  ],

  "node_order": ["head", "chest"],

  "joints": [
    {
      "name": "spine",
      "from": "chest",
      "to": "head",
      "distance": 0.8,
      "stiffness": 1.0,
      "options": {
        "invert": true,
        "parts": [
          { "name": "head", "lookup": "field" }
        ]
      }
    }
  ]
}
```

# 4. Resolving ModelParts
You can attach visible model parts using:
"lookup": "field"
→ Direct public or private field (model.head, model.body, etc.)
Works on vanilla-style models.
"lookup": "child"
→ Uses a getter such as:

```
model.getChild("head");
```

Use this for models built with:

```
TexturedModelData
```

```
ModelPart root = ...
```

Models that use named hierarchy children

# 5. Testing the setup
In-game:
Spawn your entity and kill it
Check:
Limbs connected correctly
Parts are attached
Rotations behave as expected
Console will print warnings if:
A ModelPart cannot be found
A joint references missing nodes
JSON is malformed

# 6. API Included in This Repository
The /api folder contains the minimal Java code that mods need:
✔ RagdollModelRegistry
Registers model → string key mappings.
✔ RagdollModelRegistrar
Entrypoint interface (Fabric).
✔ JSON schema reference
(just for clarity, optional)
Modders do not need the entire Ragdolls source code—only these public API classes.

# 7. How Other Mods Integrate
Step 1: Add dependency on your API JAR (not your entire mod).
Step 2: Add a corpse JSON inside their mod.
Step 3: Register their model class using your API.
That’s it.

# 8. Example: Full Integration
MyModClient.java:
```
public class MyModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        // link model ID to model class
        RagdollModelRegistry.register(
            "mymod:warrior",
            WarriorEntityModel.class
        );
    }
}
```

assets/mymod/corpses/warrior.json:
```
{
  "model": "mymod:warrior",
  "nodes": [...],
  "joints": [...]
}
```
