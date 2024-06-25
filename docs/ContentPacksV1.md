# Creating Content Packs for LandOfSignals V1

Creating content packs for LandOfSignals V1 allows you to add your own custom signals, signs, and other assets to the game. This guide will walk you through the process of creating a basic content pack.

## Content Pack Structure

A content pack for LandOfSignals V1 consists of a directory containing the following:

- `pack.json`: A JSON file containing metadata about the content pack.
- `assets`: A directory containing the models, textures, and other assets for your content pack.

### pack.json

The `pack.json` file contains metadata about your content pack. Here is an example structure:

```json
{
  "name": "My Custom Content Pack",
  "version": "1.0",
  "author": "YourName",
  "description": "A description of your content pack."
}
```

### Assets Directory

The `assets` directory should contain all the models, textures, and other assets for your content pack. Organize your assets into subdirectories for better management. For example:

- `assets/models`: Contains the 3D models for your signals and signs.
- `assets/textures`: Contains the textures for your models.

## Creating a Signal

To create a signal, you need to:

1. Model your signal using a 3D modeling software that can export to the OBJ format.
2. Create textures for your model.
3. Place your model and textures in the appropriate directories within the `assets` directory.
4. Define your signal in a JSON file within the `assets/models` directory.

### Signal Definition

A signal is defined using a JSON file. Here is an example definition:

```json
{
  "id": "custom_signal",
  "name": "Custom Signal",
  "model": "models/custom_signal.obj",
  "textures": {
    "texture": "textures/custom_signal.png"
  }
}
```

## Best Practices

- Keep your content pack organized to make it easier to manage.
- Use descriptive names for your models, textures, and other assets.
- Test your content pack thoroughly before releasing it.

## Sharing Your Content Pack

Once you have created your content pack, you can share it with the community. Make sure to include a README file with instructions on how to install and use your content pack.

Join our [Discord community](https://discord.gg/ykAqHKYjVM) to share your content packs and get feedback from other users.
