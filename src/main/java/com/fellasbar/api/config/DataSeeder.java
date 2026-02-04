package com.fellasbar.api.config;

import com.fellasbar.api.model.TargetVenue;
import com.fellasbar.api.repository.TargetVenueRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final TargetVenueRepository targetVenueRepository;

    public DataSeeder(TargetVenueRepository targetVenueRepository) {
        this.targetVenueRepository = targetVenueRepository;
    }

    @Override
    public void run(String... args) {
        if (targetVenueRepository.count() == 0) {
            System.out.println("Seeding target venues...");
            targetVenueRepository.saveAll(getTargetVenues());
            System.out.println("Seeded " + targetVenueRepository.count() + " target venues.");
        } else {
            System.out.println("Target venues already exist, skipping seed.");
        }
    }

    private List<TargetVenue> getTargetVenues() {
        return List.of(
            // ============================================
            // BREWERIES - St. Petersburg
            // ============================================
            new TargetVenue("3 Daughters Brewing", "Brewery", "222 22nd St S, St. Petersburg, FL 33712", "https://3dbrewing.com"),
            new TargetVenue("Green Bench Brewing Company", "Brewery", "1133 Baum Ave N, St. Petersburg, FL 33705", "https://greenbenchbrewing.com"),
            new TargetVenue("Cycle Brewing", "Brewery", "534 Central Ave, St. Petersburg, FL 33701", "https://cyclebrewing.com"),
            new TargetVenue("Avid Brew Company", "Brewery", "1745 1st Ave S, St. Petersburg, FL 33712", "https://avidbrew.com"),
            new TargetVenue("Cage Brewing", "Brewery", "2001 1st Ave S, St. Petersburg, FL 33712", "https://cagebrewing.com"),
            new TargetVenue("Flying Boat Brewing Company", "Brewery", "1776 11th Ave N, St. Petersburg, FL 33713", "https://flyingboatbrewing.com"),
            new TargetVenue("Grand Central Brewhouse", "Brewery", "2340 Central Ave, St. Petersburg, FL 33713", "https://grandcentralbrewhouse.com"),
            new TargetVenue("Overflow Brewing Company", "Brewery", "2012 Central Ave, St. Petersburg, FL 33712", "https://overflowbeer.com"),
            new TargetVenue("Bayboro Brewing Co.", "Brewery", "St. Petersburg, FL", null),
            new TargetVenue("Dissent Craft Brewing Co.", "Brewery", "St. Petersburg, FL", "https://dissentcraftbrewing.com"),
            new TargetVenue("Golden Isles Brewing Co.", "Brewery", "St. Petersburg, FL", null),
            new TargetVenue("If I Brewed the World", "Brewery", "St. Petersburg, FL", null),
            new TargetVenue("Juicy Brewing Co.", "Brewery", "St. Petersburg, FL", null),
            new TargetVenue("Mastry's Brewing Co.", "Brewery", "St. Pete Beach, FL", "https://mastrysbrewingco.com"),
            new TargetVenue("Gulfport Brewery + Eatery", "Brewery", "Gulfport, FL", "https://gulfportbrewery.com"),

            // ============================================
            // BREWERIES - Tampa
            // ============================================
            new TargetVenue("Cigar City Brewing", "Brewery", "3924 W Spruce St, Tampa, FL 33607", "https://cigarcitybrewing.com"),
            new TargetVenue("Angry Chair Brewing", "Brewery", "Tampa, FL", "https://angrychairbrewing.com"),
            new TargetVenue("Coppertail Brewing Co.", "Brewery", "2601 E 2nd Ave, Tampa, FL 33605", "https://coppertailbrewing.com"),
            new TargetVenue("Hidden Springs Ale Works", "Brewery", "Tampa, FL", "https://hiddenspringsaleworks.com"),
            new TargetVenue("Tampa Bay Brewing Company", "Brewery", "Tampa, FL", "https://tampabaybrewingcompany.com"),
            new TargetVenue("Magnanimous Brewing", "Brewery", "Tampa, FL", null),
            new TargetVenue("BarrieHaus Beer Co.", "Brewery", "Tampa, FL", "https://barriehaus.com"),
            new TargetVenue("Woven Water Brewing Co.", "Brewery", "Tampa, FL", "https://wovenwaterbrewing.com"),
            new TargetVenue("Deviant Libation", "Brewery", "Tampa, FL", null),
            new TargetVenue("Late Start Brewing", "Brewery", "Tampa, FL", null),
            new TargetVenue("Sky Puppy Brewing", "Brewery", "Tampa, FL", null),
            new TargetVenue("Ology Brewing Co. Tampa", "Brewery", "Tampa, FL", "https://ologybrewing.com"),
            new TargetVenue("Bootleggers Brewing Co.", "Brewery", "Tampa, FL", null),
            new TargetVenue("Channelside Brewing Co.", "Brewery", "Tampa, FL", null),
            new TargetVenue("Common Dialect Beerworks", "Brewery", "Tampa, FL", null),
            new TargetVenue("Florida Avenue Brewing Co.", "Brewery", "Tampa, FL", "https://floridaavebrewing.com"),
            new TargetVenue("Bay Cannon Beer Company", "Brewery", "Tampa, FL", "https://baycannonbeercompany.com"),
            new TargetVenue("Ulele Spring Brewery", "Brewery", "Tampa, FL", "https://ulele.com"),

            // ============================================
            // BREWERIES - Dunedin / Clearwater / Pinellas
            // ============================================
            new TargetVenue("7venth Sun Brewing", "Brewery", "Dunedin, FL", "https://7venthsun.com"),
            new TargetVenue("Dunedin Brewery", "Brewery", "Dunedin, FL", "https://dunedinbrewery.com"),
            new TargetVenue("Dunedin House of Beer", "Brewery", "Dunedin, FL", null),
            new TargetVenue("Caledonia Brewing", "Brewery", "Dunedin, FL", "https://caledoniabrewing.com"),
            new TargetVenue("Cueni Brewing Co.", "Brewery", "Dunedin, FL", null),
            new TargetVenue("Hob Brewing Co.", "Brewery", "Dunedin, FL", null),
            new TargetVenue("Big Storm Brewing Co.", "Brewery", "Clearwater, FL", "https://bigstormbrewery.com"),
            new TargetVenue("Grindhaus Brew Lab", "Brewery", "Clearwater, FL", null),
            new TargetVenue("Arkane Aleworks", "Brewery", "Largo, FL", "https://arkanealeworks.com"),
            new TargetVenue("Commerce Brewing", "Brewery", "Largo, FL", null),
            new TargetVenue("Mad Beach Craft Brewing", "Brewery", "Madeira Beach, FL", "https://madbeachbrewing.com"),
            new TargetVenue("Sea Dog Brewing Company", "Brewery", "Treasure Island, FL", null),
            new TargetVenue("Anecdote Brewing Co.", "Brewery", "Indian Rocks Beach, FL", null),
            new TargetVenue("Crooked Thumb Brewery", "Brewery", "Safety Harbor, FL", "https://crookedthumbbrewery.com"),
            new TargetVenue("De Bine Brewing Co.", "Brewery", "Palm Harbor, FL", null),
            new TargetVenue("Wild Rover Brewing Company", "Brewery", "Pinellas County, FL", null),

            // ============================================
            // BREWERIES - Other Tampa Bay
            // ============================================
            new TargetVenue("3 Keys Brewing", "Brewery", "Bradenton, FL", null),
            new TargetVenue("Big Top Brewing", "Brewery", "Sarasota, FL", "https://bigtopbrewing.com"),
            new TargetVenue("Calusa Brewing", "Brewery", "Sarasota, FL", "https://calusabrewing.com"),
            new TargetVenue("Good Liquid Brewing Co.", "Brewery", "Sarasota, FL", null),
            new TargetVenue("Motorworks Brewing", "Brewery", "Bradenton, FL", "https://motorworksbrewing.com"),
            new TargetVenue("Brew Life Brewing", "Brewery", "Sarasota, FL", null),
            new TargetVenue("Corporate Ladder Brewing Company", "Brewery", "Palmetto, FL", null),
            new TargetVenue("Brighter Days Brew Co.", "Brewery", "Tarpon Springs, FL", null),
            new TargetVenue("Cotee River Brewing", "Brewery", "New Port Richey, FL", null),
            new TargetVenue("Liquid Garage Co.", "Brewery", "New Port Richey, FL", null),
            new TargetVenue("Dented Keg Ale Works", "Brewery", "New Port Richey, FL", null),
            new TargetVenue("Infusion Brewing Co.", "Brewery", "New Port Richey, FL", null),
            new TargetVenue("Craft Life Brewing", "Brewery", "Hudson, FL", null),
            new TargetVenue("Marker 48 Brewing", "Brewery", "Spring Hill, FL", "https://marker48brewing.com"),
            new TargetVenue("Escape Brewing Co.", "Brewery", "Trinity, FL", null),
            new TargetVenue("In the Loop Brewing", "Brewery", "Land O' Lakes, FL", null),
            new TargetVenue("Leaven Brewing", "Brewery", "Riverview, FL", null),
            new TargetVenue("Bullfrog Creek Brewing Co.", "Brewery", "Valrico, FL", null),
            new TargetVenue("Keel Farms Agrarian Ale + Cider", "Brewery", "Plant City, FL", "https://keelfarms.com"),
            new TargetVenue("Brew Hub", "Brewery", "Lakeland, FL", "https://brewhub.net"),
            new TargetVenue("Dade City Brew House", "Brewery", "Dade City, FL", null),
            new TargetVenue("Front Page Brewing Co.", "Brewery", "Bartow, FL", null),

            // ============================================
            // BARS - Downtown St. Petersburg
            // ============================================
            new TargetVenue("The Ale and the Witch", "Bar", "111 2nd Ave NE, St. Petersburg, FL 33701", "https://thealeandthewitch.com"),
            new TargetVenue("Mandarin Hide", "Bar", "231 Central Ave, St. Petersburg, FL 33701", "https://mandarinhide.com"),
            new TargetVenue("Copper Shaker", "Bar", "186 4th Ave NE, St. Petersburg, FL 33701", "https://coppershaker.com"),
            new TargetVenue("The Bier Boutique", "Bar", "465 7th Ave N, St. Petersburg, FL 33701", "https://bierboutique.com"),
            new TargetVenue("Five Bucks Drinkery", "Bar", "247 Central Ave, St. Petersburg, FL 33701", "https://fivebucksdrinkery.com"),
            new TargetVenue("No Vacancy", "Bar", "937 Central Ave, St. Petersburg, FL 33705", null),
            new TargetVenue("Intermezzo Coffee & Cocktails", "Bar", "1111 Central Ave, St. Petersburg, FL 33705", null),
            new TargetVenue("The Galley", "Bar", "27 4th St N, St. Petersburg, FL 33701", null),
            new TargetVenue("Park & Rec", "Bar", "100 4th St S, St. Petersburg, FL 33701", "https://parkandrecstpete.com"),
            new TargetVenue("Ruby's Elixir", "Bar", "15 6th St N, St. Petersburg, FL 33701", "https://rubyselixir.com"),
            new TargetVenue("Saigon Blonde", "Bar", "St. Petersburg, FL", null),
            new TargetVenue("The Dog Bar", "Bar", "St. Petersburg, FL", "https://thedogbarstpete.com"),
            new TargetVenue("Dirty Laundry", "Bar", "St. Petersburg, FL", null),
            new TargetVenue("The One Night Stand", "Bar", "St. Petersburg, FL", null),
            new TargetVenue("Crafty Squirrel", "Bar", "St. Petersburg, FL", "https://craftysquirrel.com"),
            new TargetVenue("The Toasted Monkey", "Bar", "St. Petersburg, FL", null),
            new TargetVenue("Lost & Found", "Bar", "St. Petersburg, FL", null),
            new TargetVenue("The Bends", "Bar", "St. Petersburg, FL", null),
            new TargetVenue("The Zoo Club", "Bar", "St. Petersburg, FL", null),
            new TargetVenue("Floridian Social Club", "Bar", "688 Central Ave, St. Petersburg, FL 33701", null),
            new TargetVenue("The Canopy at The Birchwood", "Bar", "340 Beach Dr NE, St. Petersburg, FL 33701", "https://thebirchwood.com/canopy"),

            // ============================================
            // DIVE BARS - St. Petersburg
            // ============================================
            new TargetVenue("Flamingo Bar", "Bar", "1230 9th St N, St. Petersburg, FL", null),
            new TargetVenue("Mastry's Bar", "Bar", "233 Central Ave, St. Petersburg, FL", null),
            new TargetVenue("The Emerald Bar", "Bar", "550 Central Ave, St. Petersburg, FL 33701", null),
            new TargetVenue("Brandy's", "Bar", "7220 4th St N, St. Petersburg, FL", null),
            new TargetVenue("Dead Bob's", "Bar", "6702 Central Ave, St. Petersburg, FL", null),
            new TargetVenue("Jwags St. Pete", "Bar", "2312 4th St N, St. Petersburg, FL", null),
            new TargetVenue("Lucky Star Lounge", "Bar", "2760 Central Ave, St. Petersburg, FL", null),
            new TargetVenue("49th Street Pub", "Bar", "860 49th St N, St. Petersburg, FL", null),
            new TargetVenue("Boardwalk Tavern", "Bar", "2600 54th Ave N, St. Petersburg, FL", null),
            new TargetVenue("Bar Mastiff", "Bar", "4021 54th Ave N, St. Petersburg, FL", null),
            new TargetVenue("Smugglers", "Bar", "1120 Pinellas Bayway S, Tierra Verde, FL", null),
            new TargetVenue("One-Eyed Kellee's", "Bar", "St. Petersburg, FL", null),
            new TargetVenue("Pelican Pub", "Bar", "St. Petersburg, FL", null),
            new TargetVenue("Steve's Tavern", "Bar", "St. Petersburg, FL", null),
            new TargetVenue("Swigwam Beach Bar", "Bar", "St. Petersburg, FL", null),

            // ============================================
            // DIVE BARS - Tampa
            // ============================================
            new TargetVenue("The Hub", "Bar", "719 N Franklin St, Tampa, FL", null),
            new TargetVenue("Corner Club", "Bar", "502 E Sligh Ave, Tampa, FL", null),
            new TargetVenue("American Legion Seminole Post 111", "Bar", "6918 N Florida Ave, Tampa, FL", null),
            new TargetVenue("Stoney's", "Bar", "1305 S 22nd St, Tampa, FL", null),
            new TargetVenue("Tiny Tap Tavern", "Bar", "2105 W Morrison Ave, Tampa, FL", null),
            new TargetVenue("Hole in the Wall", "Bar", "1735 W Hillsborough Ave, Tampa, FL", null),
            new TargetVenue("Warehouse Liquor Store and Bar", "Bar", "Gandy Blvd, Tampa, FL", null),
            new TargetVenue("Dirty Shame", "Bar", "Ybor City, Tampa, FL", null),
            new TargetVenue("Reservoir Bar", "Bar", "Ybor City, Tampa, FL", null),

            // ============================================
            // SPORTS BARS
            // ============================================
            new TargetVenue("Ferg's Sports Bar & Grill", "Sports Bar", "1320 Central Ave, St. Petersburg, FL 33705", "https://fergssportsbar.com"),
            new TargetVenue("MacDinton's Irish Pub", "Sports Bar", "242 1st Ave N, St. Petersburg, FL 33701", "https://macdintons.com"),
            new TargetVenue("Hops & Props", "Sports Bar", "10400 Roosevelt Blvd N, St. Petersburg, FL 33716", null),
            new TargetVenue("Courtside Grille", "Sports Bar", "10682 Gandy Blvd, St. Petersburg, FL 33702", "https://courtsidegrille.com"),
            new TargetVenue("Engine No. 9", "Sports Bar", "St. Petersburg, FL", "https://engineno9.com"),
            new TargetVenue("Five Bucks Drinkery Seminole", "Sports Bar", "Seminole, FL", "https://fivebucksdrinkery.com"),
            new TargetVenue("Five Bucks Drinkery Pinellas Park", "Sports Bar", "Pinellas Park, FL", "https://fivebucksdrinkery.com"),
            new TargetVenue("Eddie's Bar & Grill Dunedin", "Sports Bar", "Dunedin, FL", null),
            new TargetVenue("Eddie's Bar & Grill Bay Pines", "Sports Bar", "Bay Pines, FL", null),
            new TargetVenue("Mugs Sports Bar & Grill", "Sports Bar", "Clearwater, FL", "https://mugssportsbar.com"),
            new TargetVenue("Press Box Sports", "Sports Bar", "Tampa, FL", "https://pressboxsports.com"),
            new TargetVenue("Hattrick's", "Sports Bar", "Tampa, FL", "https://hattrickstavern.com"),
            new TargetVenue("Riveters", "Sports Bar", "Tampa, FL", null),
            new TargetVenue("Whiskey Wings Oldsmar", "Sports Bar", "Oldsmar, FL", "https://whiskeywings.com"),
            new TargetVenue("Whiskey Wings Tarpon Springs", "Sports Bar", "Tarpon Springs, FL", "https://whiskeywings.com"),
            new TargetVenue("Whiskey Wings St. Pete", "Sports Bar", "St. Petersburg, FL", "https://whiskeywings.com"),
            new TargetVenue("Whiskey Wings Largo", "Sports Bar", "Largo, FL", "https://whiskeywings.com"),
            new TargetVenue("Glory Days Grill", "Sports Bar", "Tampa Bay, FL", "https://glorydaysgrill.com"),

            // ============================================
            // BEACH BARS
            // ============================================
            new TargetVenue("Jimmy B's Beach Bar", "Bar", "St. Pete Beach, FL", "https://jimmybsbeachbar.com"),
            new TargetVenue("Bongos Beach Bar and Grille", "Bar", "St. Pete Beach, FL", null),
            new TargetVenue("Undertow Beach Bar", "Bar", "St. Pete Beach, FL", "https://undertowbeachbar.com"),
            new TargetVenue("Pier Teaki", "Bar", "St. Pete Beach, FL", null),
            new TargetVenue("The Drunken Clam", "Bar", "46 46th Ave, St. Pete Beach, FL", null),
            new TargetVenue("Blue Parrot", "Bar", "85 Corey Cir, St. Pete Beach, FL", null),
            new TargetVenue("Ka'Tiki Bar", "Bar", "Sunset Beach, FL", null),
            new TargetVenue("Coconut Charlie's Beach Bar & Grill", "Bar", "St. Pete Beach, FL", null),
            new TargetVenue("Postcard Inn Beach Bar", "Bar", "St. Pete Beach, FL", null),
            new TargetVenue("Salty's Beach Lounge", "Bar", "St. Pete Beach, FL", null),
            new TargetVenue("Mahuffers", "Bar", "19201 Gulf Blvd, Indian Shores, FL", null),
            new TargetVenue("Sandbar Clearwater", "Bar", "Clearwater Beach, FL", null),
            new TargetVenue("Shephard's Tiki Beach Bar & Grill", "Bar", "Clearwater Beach, FL", "https://shephards.com"),
            new TargetVenue("Jimmy's Crow's Nest", "Bar", "Clearwater Beach, FL", null),

            // ============================================
            // PUBS
            // ============================================
            new TargetVenue("Horse and Jockey British Pub", "Pub", "St. Petersburg, FL", null),
            new TargetVenue("The Wheelhouse", "Pub", "St. Petersburg, FL", null),
            new TargetVenue("Tiki Tim's Pub and Grille", "Pub", "St. Petersburg, FL", null),
            new TargetVenue("Mad Dogs & Englishmen", "Pub", "St. Petersburg, FL", null),
            new TargetVenue("St Andrews Pub", "Pub", "St. Petersburg, FL", null),
            new TargetVenue("The Tap Room At The Hollander", "Pub", "St. Petersburg, FL", "https://hollanderhotel.com"),
            new TargetVenue("Tommy's Hideaway", "Pub", "St. Petersburg, FL", null),
            new TargetVenue("RoadHouse Pub", "Pub", "St. Petersburg, FL", null),
            new TargetVenue("Salty's Gulfport", "Pub", "5413 Shore Blvd S, Gulfport, FL 33707", "https://saltysgulfport.com"),
            new TargetVenue("O'Neill's Irish Pub", "Pub", "4924 Gulfport Blvd S, Gulfport, FL", null),
            new TargetVenue("Finley's Irish Pub & Eatery", "Pub", "Largo, FL", "https://finleysirishpub.com"),
            new TargetVenue("Abe's Place", "Pub", "1250 S Missouri Ave, Clearwater, FL", null),
            new TargetVenue("Franks", "Pub", "4201 62nd Ave N, Pinellas Park, FL", null),
            new TargetVenue("Anclote River Boat Club", "Pub", "1761 Beckett Wy, Tarpon Springs, FL", null),
            new TargetVenue("The Shipwreck", "Pub", "Clearwater, FL", null),
            new TargetVenue("Overtime Sports Bar", "Pub", "Clearwater, FL", null),

            // ============================================
            // RESTAURANTS WITH BARS - St. Pete
            // ============================================
            new TargetVenue("Noble Crust", "Restaurant", "8300 4th St N, St. Petersburg, FL 33702", "https://noblecrust.com"),
            new TargetVenue("Stillwaters Tavern", "Restaurant", "224 Beach Dr NE, St. Petersburg, FL 33701", "https://stillwaterstavern.com"),
            new TargetVenue("Tryst Gastrolounge", "Restaurant", "240 Beach Dr NE, St. Petersburg, FL 33701", "https://trystgastrolounge.com"),
            new TargetVenue("Flute & Dram", "Restaurant", "216 Beach Dr NE, St. Petersburg, FL 33701", "https://fluteanddram.com"),
            new TargetVenue("The Mill", "Restaurant", "200 Central Ave, St. Petersburg, FL 33701", null),
            new TargetVenue("The Birchwood", "Restaurant", "340 Beach Dr NE, St. Petersburg, FL 33701", "https://thebirchwood.com"),
            new TargetVenue("Cassis American Brasserie", "Restaurant", "170 Beach Dr NE, St. Petersburg, FL 33701", "https://cassisab.com"),
            new TargetVenue("Z Grille", "Restaurant", "104 2nd St S, St. Petersburg, FL 33701", "https://zgrille.com"),
            new TargetVenue("The Avenue", "Restaurant", "330 1st Ave S, St. Petersburg, FL 33701", null),
            new TargetVenue("Sea Salt", "Restaurant", "183 2nd Ave N, St. Petersburg, FL 33701", "https://seasaltstpete.com"),
            new TargetVenue("Red Mesa Cantina", "Restaurant", "128 3rd St S, St. Petersburg, FL 33701", "https://redmesacantina.com"),
            new TargetVenue("Parkshore Grill", "Restaurant", "300 Beach Dr NE, St. Petersburg, FL 33701", "https://parkshoregrill.com"),
            new TargetVenue("Ceviche Tapas Bar", "Restaurant", "10 Beach Dr, St. Petersburg, FL 33701", "https://ceviche.com"),
            new TargetVenue("The Blu Halo", "Restaurant", "St. Petersburg, FL", null),
            new TargetVenue("Green Pagoda", "Restaurant", "St. Petersburg, FL", null),
            new TargetVenue("Grand Hacienda", "Restaurant", "St. Petersburg, FL", null),

            // ============================================
            // RESTAURANTS WITH BARS - Clearwater
            // ============================================
            new TargetVenue("Frenchy's Rockaway Grill", "Restaurant", "Clearwater Beach, FL", "https://frenchysonline.com"),
            new TargetVenue("Salty's Island Bar & Grille", "Restaurant", "Clearwater Beach, FL", "https://saltysisland.com"),
            new TargetVenue("Palm Pavilion Beachside Grill & Bar", "Restaurant", "Clearwater Beach, FL", "https://palmpavilion.com"),
            new TargetVenue("Bob Heilman's Beachcomber", "Restaurant", "Clearwater Beach, FL", "https://bobheilmans.com"),
            new TargetVenue("Clear Sky Global Bistro", "Restaurant", "Clearwater, FL", null),
            new TargetVenue("E&E Stakeout Grill", "Restaurant", "Clearwater, FL", null),
            new TargetVenue("The Deep End", "Restaurant", "Clearwater Beach, FL", null),
            new TargetVenue("Rumba Island Bar & Grill", "Restaurant", "Clearwater, FL", "https://rumbaisland.com"),
            new TargetVenue("Cafe Ponte", "Restaurant", "13505 Icot Blvd, Clearwater, FL 33760", "https://cafeponte.com"),
            new TargetVenue("Original Hooters Clearwater", "Restaurant", "Clearwater, FL", "https://hooters.com"),

            // ============================================
            // WINERIES
            // ============================================
            new TargetVenue("Florida Orange Groves Winery", "Winery", "1500 Pasadena Ave S, St. Petersburg, FL 33707", "https://floridawine.com"),
            new TargetVenue("Keel & Curley Winery", "Winery", "Plant City, FL", "https://keelcurley.com"),

            // ============================================
            // COCKTAIL LOUNGES
            // ============================================
            new TargetVenue("Tipsy Tiki", "Bar", "St. Petersburg, FL", null),
            new TargetVenue("Reefers Social Club", "Bar", "Clearwater, FL", null),
            new TargetVenue("Sielo Rooftop Bar", "Bar", "Clearwater, FL", null),
            new TargetVenue("Monkey Bar of Clearwater", "Bar", "Clearwater, FL", null),
            new TargetVenue("45 Sports Bar and Lounge", "Bar", "Clearwater, FL", null),
            new TargetVenue("Sunset Lounge", "Bar", "Clearwater, FL", null),
            new TargetVenue("Hi-Fi", "Bar", "Clearwater, FL", null),

            // ============================================
            // ADDITIONAL TAMPA BARS
            // ============================================
            new TargetVenue("The Independent", "Bar", "Tampa, FL", "https://independenttampa.com"),
            new TargetVenue("The Blind Tiger", "Bar", "Ybor City, Tampa, FL", null),
            new TargetVenue("Gaspar's Grotto", "Bar", "Ybor City, Tampa, FL", "https://gasparsgrotto.com"),
            new TargetVenue("The Bricks Ybor", "Bar", "Ybor City, Tampa, FL", null),
            new TargetVenue("Bad Monkey", "Bar", "Ybor City, Tampa, FL", null),
            new TargetVenue("The Castle", "Bar", "Ybor City, Tampa, FL", null),
            new TargetVenue("Ciro's Speakeasy", "Bar", "Tampa, FL", "https://cirosspeakeasy.com"),
            new TargetVenue("The Attic", "Bar", "Tampa, FL", null),
            new TargetVenue("Hyde Park Cafe", "Bar", "Tampa, FL", null),
            new TargetVenue("Fly Bar", "Bar", "Tampa, FL", "https://flybargroup.com"),
            new TargetVenue("Haven", "Bar", "Tampa, FL", "https://havenrooftop.com"),
            new TargetVenue("Lowry Parcade & Tavern", "Bar", "Tampa, FL", "https://lowryparcade.com"),
            new TargetVenue("Datz Dough", "Bar", "Tampa, FL", "https://datz.com"),
            new TargetVenue("The Refinery", "Restaurant", "Tampa, FL", "https://thetamparefinery.com"),
            new TargetVenue("Rooster & the Till", "Restaurant", "Tampa, FL", "https://roosterandthetill.com"),
            new TargetVenue("Bern's Steak House", "Restaurant", "Tampa, FL", "https://bernssteakhouse.com"),
            new TargetVenue("Columbia Restaurant", "Restaurant", "Ybor City, Tampa, FL", "https://columbiarestaurant.com"),
            new TargetVenue("Ulele", "Restaurant", "Tampa, FL", "https://ulele.com"),
            new TargetVenue("Armature Works", "Restaurant", "Tampa, FL", "https://armatureworks.com"),
            new TargetVenue("Oxford Exchange", "Restaurant", "Tampa, FL", "https://oxfordexchange.com"),

            // ============================================
            // ADDITIONAL PINELLAS VENUES
            // ============================================
            new TargetVenue("JJ's Market & Deli", "Restaurant", "St. Petersburg, FL", null),
            new TargetVenue("The Brothers Thomas Bespoke Cafe", "Restaurant", "St. Petersburg, FL", null),
            new TargetVenue("Root + Clay", "Restaurant", "St. Petersburg, FL", null),
            new TargetVenue("Crab Shack Restaurant", "Restaurant", "St. Petersburg, FL", null),
            new TargetVenue("Patrona Coastal Cafe", "Restaurant", "St. Petersburg, FL", null),
            new TargetVenue("Sushi Spot Hibachi", "Restaurant", "Largo, FL", null),

            // ============================================
            // DUNEDIN BARS & RESTAURANTS
            // ============================================
            new TargetVenue("Soggy Bottom Bar", "Bar", "Dunedin, FL", null),
            new TargetVenue("The Living Room on Main", "Bar", "Dunedin, FL", null),
            new TargetVenue("Flanagan's Irish Pub", "Pub", "Dunedin, FL", null),
            new TargetVenue("The Black Pearl", "Restaurant", "Dunedin, FL", null),
            new TargetVenue("Casa Tina", "Restaurant", "Dunedin, FL", null),
            new TargetVenue("Bon Appetit", "Restaurant", "Dunedin, FL", null),
            new TargetVenue("Kelly's Chic A Boom Room", "Restaurant", "Dunedin, FL", null),

            // ============================================
            // TARPON SPRINGS
            // ============================================
            new TargetVenue("Mr. Mike's", "Bar", "Tarpon Springs, FL", null),
            new TargetVenue("Rusty Bellies", "Restaurant", "Tarpon Springs, FL", "https://rustybellies.com"),
            new TargetVenue("Hellas Restaurant", "Restaurant", "Tarpon Springs, FL", "https://hellasrestaurant.com"),
            new TargetVenue("Mykonos", "Restaurant", "Tarpon Springs, FL", null),
            new TargetVenue("Costa's Restaurant", "Restaurant", "Tarpon Springs, FL", null),

            // ============================================
            // SAFETY HARBOR / OLDSMAR
            // ============================================
            new TargetVenue("Nolan's Pub", "Pub", "Safety Harbor, FL", null),
            new TargetVenue("8th Avenue Pub", "Pub", "Safety Harbor, FL", null),
            new TargetVenue("Whistle Stop Bar & Grill", "Bar", "Safety Harbor, FL", null),
            new TargetVenue("Green Springs Bistro", "Restaurant", "Safety Harbor, FL", null),
            new TargetVenue("Southern Fresh", "Restaurant", "Safety Harbor, FL", null),

            // ============================================
            // TREASURE ISLAND / MADEIRA BEACH
            // ============================================
            new TargetVenue("VIP Lounge", "Bar", "Treasure Island, FL", null),
            new TargetVenue("The Flounder", "Bar", "Treasure Island, FL", null),
            new TargetVenue("Caddy's Treasure Island", "Bar", "Treasure Island, FL", "https://caddys.com"),
            new TargetVenue("Ricky T's", "Restaurant", "Treasure Island, FL", null),
            new TargetVenue("Sculley's", "Restaurant", "Madeira Beach, FL", "https://sculleysboardwalk.com"),
            new TargetVenue("The Boardwalk Grille", "Restaurant", "Madeira Beach, FL", null),
            new TargetVenue("Dockside Dave's", "Restaurant", "Madeira Beach, FL", null),

            // ============================================
            // INDIAN ROCKS BEACH / INDIAN SHORES
            // ============================================
            new TargetVenue("Crabby Bill's", "Restaurant", "Indian Rocks Beach, FL", "https://crabbybills.com"),
            new TargetVenue("Keegan's Seafood Grille", "Restaurant", "Indian Rocks Beach, FL", "https://keegansirbseafood.com"),
            new TargetVenue("JD's Restaurant", "Restaurant", "Indian Rocks Beach, FL", null),
            new TargetVenue("Guppy's on the Beach", "Restaurant", "Indian Rocks Beach, FL", "https://guppysonthebeach.com"),
            new TargetVenue("The Pub Waterfront", "Pub", "Indian Rocks Beach, FL", "https://thepubwaterfront.com"),
            new TargetVenue("Salt Rock Grill", "Restaurant", "Indian Shores, FL", "https://saltrockgrill.com"),

            // ============================================
            // SEMINOLE / PINELLAS PARK / LARGO
            // ============================================
            new TargetVenue("Beef O' Brady's Seminole", "Sports Bar", "Seminole, FL", null),
            new TargetVenue("The Getaway", "Bar", "Seminole, FL", null),
            new TargetVenue("Bananas Beachside Bar & Grill", "Bar", "Seminole, FL", null),
            new TargetVenue("Taco Bus Largo", "Restaurant", "Largo, FL", "https://tacobus.com"),
            new TargetVenue("The Thai Place", "Restaurant", "Largo, FL", null),
            new TargetVenue("Acropolis Greek Taverna", "Restaurant", "Largo, FL", "https://acropolistaverna.com")
        );
    }
}
