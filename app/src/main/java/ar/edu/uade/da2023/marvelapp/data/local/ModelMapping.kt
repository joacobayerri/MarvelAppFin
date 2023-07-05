package ar.edu.uade.da2023.marvelapp.data.local

import ar.edu.uade.da2023.marvelapp.model.ItemCount
import ar.edu.uade.da2023.marvelapp.model.Thumbnail

fun ar.edu.uade.da2023.marvelapp.model.Character.toLocal() = CharacterLocal(
    id = id,
    name = name,
    thumbnail_path = thumbnail.path,
    thumbnail_ex = thumbnail.extension,
    description = description,
    series = series.available,
    comics = comics.available,
    stories = stories.available,
    events = events.available
)

fun CharacterLocal.toExternal() = ar.edu.uade.da2023.marvelapp.model.Character(
    id = id,
    name = name,
    thumbnail = Thumbnail(thumbnail_path, thumbnail_ex),
    description = description,
    series = ItemCount(series),
    comics = ItemCount(comics),
    stories = ItemCount(stories),
    events = ItemCount(events)
)