package com.cs407.wellnest.data

val birdData = mapOf(
    "American Bald Eagle" to BirdData(
        description = "A symbol of the United States, this large raptor has a white head and tail, with a powerful build.",
        lat1 = 24.0,
        lat2 = 34.0,
        lon1 = -125.0,
        lon2 = -115.0
    ),
    "Northern Cardinal" to BirdData(
        description = "A vibrant red songbird with a distinctive crest and black face mask around its beak.",
        lat1 = 34.0,
        lat2 = 44.0,
        lon1 = -125.0,
        lon2 = -115.0
    ),
    "Blue Jay" to BirdData(
        description = "Known for its bright blue feathers and loud, jay! jay! call.",
        lat1 = 44.0,
        lat2 = 54.0,
        lon1 = -125.0,
        lon2 = -115.0
    ),
    "American Robin" to BirdData(
        description = "A common songbird with a red-orange breast, found in lawns and gardens.",
        lat1 = 54.0,
        lat2 = 64.0,
        lon1 = -125.0,
        lon2 = -115.0
    ),
    "Golden Eagle" to BirdData(
        description = "A powerful predator with dark brown plumage and golden feathers on the back of its head.",
        lat1 = 64.0,
        lat2 = 74.0,
        lon1 = -125.0,
        lon2 = -115.0
    ),
    "Peregrine Falcon" to BirdData(
        description = "The fastest bird, known for its high-speed dives and slate gray feathers.",
        lat1 = 24.0,
        lat2 = 34.0,
        lon1 = -115.0,
        lon2 = -105.0
    ),
    "Snowy Owl" to BirdData(
        description = "A large, white owl with yellow eyes, often found in open tundra.",
        lat1 = 34.0,
        lat2 = 44.0,
        lon1 = -115.0,
        lon2 = -105.0
    ),
    "Mallard Duck" to BirdData(
        description = "A common duck with a shiny green head (male) and brown-speckled body (female).",
        lat1 = 44.0,
        lat2 = 54.0,
        lon1 = -115.0,
        lon2 = -105.0
    ),
    "Ruby-throated Hummingbird" to BirdData(
        description = "A tiny bird with iridescent green feathers and a red throat (male).",
        lat1 = 54.0,
        lat2 = 64.0,
        lon1 = -115.0,
        lon2 = -105.0
    ),
    "Sandhill Crane" to BirdData(
        description = "A tall, gray bird with a red crown, known for its trumpeting calls and migrations.",
        lat1 = 64.0,
        lat2 = 74.0,
        lon1 = -115.0,
        lon2 = -105.0
    ),
    "Great Horned Owl" to BirdData(
        description = "A large owl with prominent ear tufts and a deep hooting voice.",
        lat1 = 24.0,
        lat2 = 34.0,
        lon1 = -105.0,
        lon2 = -95.0
    ),
    "Eastern Bluebird" to BirdData(
        description = "A small thrush with bright blue plumage and an orange chest.",
        lat1 = 34.0,
        lat2 = 44.0,
        lon1 = -105.0,
        lon2 = -95.0
    ),
    "House Sparrow" to BirdData(
        description = "A small, brown-streaked bird common in urban and suburban areas.",
        lat1 = 44.0,
        lat2 = 54.0,
        lon1 = -105.0,
        lon2 = -95.0
    ),
    "California Condor" to BirdData(
        description = "One of the largest flying birds, known for its impressive wingspan and black plumage.",
        lat1 = 54.0,
        lat2 = 64.0,
        lon1 = -105.0,
        lon2 = -95.0
    ),
    "Red-tailed Hawk" to BirdData(
        description = "A common raptor with a distinctive red tail, often seen soaring in circles.",
        lat1 = 64.0,
        lat2 = 74.0,
        lon1 = -105.0,
        lon2 = -95.0
    ),
    "American Kestrel" to BirdData(
        description = "A small falcon with a colorful plumage, often perched on wires.",
        lat1 = 24.0,
        lat2 = 34.0,
        lon1 = -95.0,
        lon2 = -85.0
    ),
    "Great Blue Heron" to BirdData(
        description = "A tall wading bird with gray-blue feathers and a long neck.",
        lat1 = 34.0,
        lat2 = 44.0,
        lon1 = -95.0,
        lon2 = -85.0
    ),
    "Baltimore Oriole" to BirdData(
        description = "A striking orange and black bird known for its melodious songs.",
        lat1 = 44.0,
        lat2 = 54.0,
        lon1 = -95.0,
        lon2 = -85.0
    ),
    "Common Loon" to BirdData(
        description = "A diving bird with a haunting call and striking black-and-white plumage.",
        lat1 = 54.0,
        lat2 = 64.0,
        lon1 = -95.0,
        lon2 = -85.0
    ),
    "Black-capped Chickadee" to BirdData(
        description = "A small, round bird with a black cap and bib, known for its chick-a-dee-dee-dee call.",
        lat1 = 64.0,
        lat2 = 74.0,
        lon1 = -95.0,
        lon2 = -85.0
    )
)

data class BirdData(
    val description: String,
    val lat1: Double,
    val lat2: Double,
    val lon1: Double,
    val lon2: Double
)