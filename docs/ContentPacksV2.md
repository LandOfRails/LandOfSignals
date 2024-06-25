# Creating and Customizing Content Packs for LandOfSignals V2

Creating and customizing content packs for LandOfSignals V2 allows mod users to add their own unique signals, signs, and other assets to the game, enhancing the immersive experience with the LandOfSignals theme. This guide will walk you through the process of creating a content pack for version 2 of the mod, ensuring consistency with the LandOfSignals theme.

## Content Pack Structure

A content pack for LandOfSignals V2 consists of a directory containing the following:

- `pack.json`: A JSON file containing metadata about the content pack.
- `assets`: A directory containing the models, textures, and other assets for your content pack.

### pack.json

The `pack.json` file contains metadata about your content pack. Here is an example structure:

```json
{
  "name": "My Custom Content Pack",
  "version": "2.0",
  "author": "YourName",
  "description": "A description of your content pack, aligned with the LandOfSignals theme."
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
  "id": "custom_signal_v2",
  "name": "Custom Signal V2",
  "model": "models/custom_signal_v2.obj",
  "textures": {
    "texture": "textures/custom_signal_v2.png"
  }
}
```

## Best Practices

- Keep your content pack organized to make it easier to manage.
- Use descriptive names for your models, textures, and other assets.
- Test your content pack thoroughly before releasing it.

## Sharing Your Content Pack

Once you have created your content pack, you can share it with the community. Make sure to include a README file with instructions on how to install and use your content pack, ensuring it aligns with the LandOfSignals theme.

Join our [Discord community](https://discord.gg/ykAqHKYjVM) to share your content packs and get feedback from other users.
