-- Seed data: 8 sample venues from FellasBarApp

-- 1. The Rusty Tap
INSERT INTO venues (id, name, type, address, latitude, longitude, phone, website, description, image_url, rating, review_count, is_open)
VALUES (1, 'The Rusty Tap', 'Bar', '123 Main St', 40.7580, -73.9855, '(555) 123-4567', 'https://therustytap.com',
        'A cozy neighborhood bar with craft cocktails, cold beers on tap, and a laid-back atmosphere perfect for unwinding after work.',
        'rusty_tap', 4.5, 234, true);

-- 2. Hopworks Brewery
INSERT INTO venues (id, name, type, address, latitude, longitude, phone, website, description, image_url, rating, review_count, is_open)
VALUES (2, 'Hopworks Brewery', 'Brewery', '456 Craft Ave', 40.7614, -73.9776, '(555) 234-5678', 'https://hopworksbrewery.com',
        'Award-winning craft brewery featuring rotating seasonal taps, a full kitchen, and weekend live music on the patio.',
        'hopworks', 4.8, 512, true);

-- 3. Downtown Grill & Bar
INSERT INTO venues (id, name, type, address, latitude, longitude, phone, website, description, image_url, rating, review_count, is_open)
VALUES (3, 'Downtown Grill & Bar', 'Restaurant', '789 Center Blvd', 40.7549, -73.9840, '(555) 345-6789', 'https://downtowngrillbar.com',
        'Upscale casual dining with an extensive cocktail list, premium steaks, and a rooftop patio overlooking downtown.',
        'downtown_grill', 4.2, 189, true);

-- 4. O''Malley''s Pub
INSERT INTO venues (id, name, type, address, latitude, longitude, phone, website, description, image_url, rating, review_count, is_open)
VALUES (4, 'O''Malley''s Pub', 'Pub', '321 Irish Way', 40.7605, -73.9933, '(555) 456-7890', 'https://omalleyspub.com',
        'Authentic Irish pub with live folk music, hearty traditional fare, and the best Guinness pour in town.',
        'omalleys', 4.4, 321, false);

-- 5. Valley View Winery
INSERT INTO venues (id, name, type, address, latitude, longitude, phone, website, description, image_url, rating, review_count, is_open)
VALUES (5, 'Valley View Winery', 'Winery', '1000 Vineyard Rd', 40.7484, -73.9967, '(555) 567-8901', 'https://valleyviewwinery.com',
        'Boutique winery with panoramic valley views, artisan cheese boards, and award-winning small-batch wines.',
        'valley_view', 4.9, 156, true);

-- 6. The End Zone
INSERT INTO venues (id, name, type, address, latitude, longitude, phone, website, description, image_url, rating, review_count, is_open)
VALUES (6, 'The End Zone', 'Sports Bar', '555 Stadium Dr', 40.7527, -73.9772, '(555) 678-9012', 'https://theendzone.com',
        'The ultimate sports bar with 30 big screens, loaded nachos, buckets of beer, and every game you could want.',
        'end_zone', 4.1, 445, true);

-- 7. Craft & Cork
INSERT INTO venues (id, name, type, address, latitude, longitude, phone, website, description, image_url, rating, review_count, is_open)
VALUES (7, 'Craft & Cork', 'Bar', '88 Vine St', 40.7590, -73.9800, '(555) 789-0123', 'https://craftandcork.com',
        'Modern cocktail bar specializing in craft spirits, natural wines, and inventive small plates in a sleek setting.',
        'craft_cork', 4.6, 278, true);

-- 8. Ironside Brewing Co.
INSERT INTO venues (id, name, type, address, latitude, longitude, phone, website, description, image_url, rating, review_count, is_open)
VALUES (8, 'Ironside Brewing Co.', 'Brewery', '202 Industrial Pkwy', 40.7450, -73.9900, '(555) 890-1234', 'https://ironsidebrewing.com',
        'Industrial-chic taproom with 20 house-brewed beers, food trucks on weekends, and a dog-friendly patio.',
        'ironside', 4.7, 389, true);


-- Operating Hours

-- The Rusty Tap
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (1, 'Monday', '4pm - 12am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (1, 'Tuesday', '4pm - 12am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (1, 'Wednesday', '4pm - 2am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (1, 'Thursday', '4pm - 2am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (1, 'Friday', '2pm - 2am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (1, 'Saturday', '12pm - 2am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (1, 'Sunday', '12pm - 10pm');

-- Hopworks Brewery
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (2, 'Monday', 'Closed');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (2, 'Tuesday', '3pm - 10pm');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (2, 'Wednesday', '3pm - 10pm');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (2, 'Thursday', '3pm - 11pm');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (2, 'Friday', '12pm - 12am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (2, 'Saturday', '11am - 12am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (2, 'Sunday', '11am - 9pm');

-- Downtown Grill & Bar
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (3, 'Monday', '11am - 10pm');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (3, 'Tuesday', '11am - 10pm');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (3, 'Wednesday', '11am - 10pm');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (3, 'Thursday', '11am - 11pm');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (3, 'Friday', '11am - 12am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (3, 'Saturday', '10am - 12am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (3, 'Sunday', '10am - 9pm');

-- O'Malley's Pub
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (4, 'Monday', '3pm - 12am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (4, 'Tuesday', '3pm - 2am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (4, 'Wednesday', '3pm - 12am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (4, 'Thursday', '3pm - 12am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (4, 'Friday', '12pm - 2am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (4, 'Saturday', '12pm - 2am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (4, 'Sunday', '12pm - 11pm');

-- Valley View Winery
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (5, 'Monday', 'Closed');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (5, 'Tuesday', 'Closed');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (5, 'Wednesday', '12pm - 8pm');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (5, 'Thursday', '12pm - 8pm');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (5, 'Friday', '12pm - 10pm');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (5, 'Saturday', '11am - 10pm');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (5, 'Sunday', '11am - 6pm');

-- The End Zone
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (6, 'Monday', '11am - 1am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (6, 'Tuesday', '11am - 12am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (6, 'Wednesday', '11am - 12am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (6, 'Thursday', '11am - 1am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (6, 'Friday', '11am - 2am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (6, 'Saturday', '10am - 2am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (6, 'Sunday', '10am - 1am');

-- Craft & Cork
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (7, 'Monday', 'Closed');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (7, 'Tuesday', '5pm - 12am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (7, 'Wednesday', '5pm - 12am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (7, 'Thursday', '5pm - 1am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (7, 'Friday', '4pm - 2am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (7, 'Saturday', '4pm - 2am');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (7, 'Sunday', '4pm - 11pm');

-- Ironside Brewing Co.
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (8, 'Monday', 'Closed');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (8, 'Tuesday', 'Closed');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (8, 'Wednesday', '3pm - 10pm');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (8, 'Thursday', '3pm - 10pm');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (8, 'Friday', '12pm - 11pm');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (8, 'Saturday', '11am - 11pm');
INSERT INTO operating_hours (venue_id, day_of_week, hours) VALUES (8, 'Sunday', '11am - 8pm');


-- Specials

-- The Rusty Tap
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (1, 'Happy Hour', '$3 domestic drafts', 6.00, 3.00, 'Daily');
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (1, 'Wing Wednesday', '50¢ wings with drink purchase', NULL, 0.50, 'Wednesday');
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (1, 'Taco Tuesday', '$2 street tacos', 5.00, 2.00, 'Tuesday');

-- Hopworks Brewery
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (2, 'Tasting Flight', '4 beers for $12', 20.00, 12.00, 'Daily');
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (2, 'Pint Night', '$4 pints all night', 7.00, 4.00, 'Thursday');
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (2, 'Brunch & Brews', 'Brunch entree + 2 beers $18', 28.00, 18.00, 'Sunday');

-- Downtown Grill & Bar
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (3, '2-for-1 Apps', 'All appetizers BOGO', NULL, 0.00, 'Monday');
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (3, 'Date Night', '2 entrees + bottle of wine $60', 90.00, 60.00, 'Friday');
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (3, 'Burger & Beer', 'Any burger + draft $14', 22.00, 14.00, 'Wednesday');

-- O'Malley's Pub
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (4, 'Trivia Tuesday', 'Free entry, win bar tabs', NULL, 0.00, 'Tuesday');
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (4, 'Fish & Chips Friday', '$12 fish & chips + pint', 22.00, 12.00, 'Friday');
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (4, 'Sunday Roast', 'Traditional roast dinner $15', 24.00, 15.00, 'Sunday');

-- Valley View Winery
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (5, 'Wine Down Sunday', 'Half-price bottles', NULL, 0.00, 'Sunday');
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (5, 'Tasting Special', '5 wines + cheese board $25', 40.00, 25.00, 'Saturday');
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (5, 'Rosé Thursday', 'All rosé by the glass $6', 12.00, 6.00, 'Thursday');

-- The End Zone
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (6, 'Game Day Buckets', '5 beers for $15', 30.00, 15.00, 'Sunday');
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (6, 'Monday Night Special', '$2 tacos + $4 margaritas', NULL, 2.00, 'Monday');
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (6, 'Wing & Pint Combo', '10 wings + pint $12', 20.00, 12.00, 'Thursday');

-- Craft & Cork
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (7, 'Industry Night', '50% off for service workers', NULL, 0.00, 'Tuesday');
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (7, 'Cocktail Class', 'Learn + drink for $20', 40.00, 20.00, 'Wednesday');
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (7, 'Natural Wine Friday', 'All natural wines $8/glass', 14.00, 8.00, 'Friday');

-- Ironside Brewing Co.
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (8, 'Growler Fill', 'Any growler fill $10', 18.00, 10.00, 'Wednesday');
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (8, 'Hoppy Hour', '$5 all IPAs', 8.00, 5.00, 'Thursday');
INSERT INTO specials (venue_id, name, description, original_price, special_price, day_of_week) VALUES (8, 'Brewer''s Brunch', 'Brunch plate + flight $22', 34.00, 22.00, 'Saturday');
