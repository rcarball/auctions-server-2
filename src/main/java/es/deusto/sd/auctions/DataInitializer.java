/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */
package es.deusto.sd.auctions;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import es.deusto.sd.auctions.dao.ArticleRepository;
import es.deusto.sd.auctions.dao.CategoryRepository;
import es.deusto.sd.auctions.dao.UserRepository;
import es.deusto.sd.auctions.entity.Article;
import es.deusto.sd.auctions.entity.Category;
import es.deusto.sd.auctions.entity.User;

@Configuration
public class DataInitializer {

	private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
	
    @Bean
    @Transactional
    CommandLineRunner initData(CategoryRepository categoryRepository, UserRepository userRepository, ArticleRepository articleRepository) {
		return args -> {
			// Database is already initialized
            if (categoryRepository.count() > 0) {                
                return;
            }			
			
			// Create some users
			User batman = new User("BruceWayne", "batman@dc.com", "Batm@n123!");
			User spiderman = new User("PeterParker", "spiderman@marvel.com", "Sp!derM4n2023");
			User superman = new User("ClarkKent", "superman@dc.com", "Sup3rm@n456!");
			User wonderWoman = new User("DianaPrince", "wonderwoman@dc.com", "Wond3rW0m@n!789");
			User captainMarvel = new User("CarolDanvers", "captainmarvel@marvel.com", "C@ptMarv3l#987");
			User blackWidow = new User("NatashaRomanoff", "blackwidow@marvel.com", "Bl@ckWid0w2023");

			// Save users
			userRepository.saveAll(List.of(batman, spiderman, superman, wonderWoman, captainMarvel, blackWidow));			
			logger.info("Users saved!");
			
			// Create some categories
			Category electronics = new Category("Electronics");
			Category sports = new Category("Sporting Goods");
			Category motors = new Category("Motors");		

			// Initialize auctions end date
			Calendar calendar = Calendar.getInstance();
			
			// Set calendar to December 31, current year
			calendar.set(Calendar.MONTH, Calendar.DECEMBER);
			calendar.set(Calendar.DAY_OF_MONTH, 31);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);			
			
			Date auctionEndDate = calendar.getTime();
			
			// Articles of Electronics category
            Article iphone = new Article("Apple iPhone 14 Pro", 999.99f, auctionEndDate, electronics, batman);
            electronics.addArticle(iphone);
            Article ps5 = new Article("Sony PlayStation 5", 499.99f, auctionEndDate, electronics, spiderman);
            electronics.addArticle(ps5);
            Article macbook = new Article("MacBook Air M2", 1199.99f, auctionEndDate, electronics, wonderWoman);
            electronics.addArticle(macbook);
            Article samsung = new Article("Samsung Galaxy S21", 799.99f, auctionEndDate, electronics, captainMarvel);
            electronics.addArticle(samsung);
            // Articles of Sporting Goods category
            Article tennisRacket = new Article("Wilson Tennis Racket", 119.99f, auctionEndDate, sports, batman);
            sports.addArticle(tennisRacket);
            Article soccerBall = new Article("Adidas Soccer Ball", 29.99f, auctionEndDate, sports, blackWidow);
            sports.addArticle(soccerBall);
            Article fitbit = new Article("Fitbit Charge 5 Fitness Tracker", 149.99f, auctionEndDate, sports, captainMarvel);
            sports.addArticle(fitbit);
            Article peloton = new Article("Peloton Exercise Bike", 1899.99f, auctionEndDate, sports, wonderWoman);
            sports.addArticle(peloton);
            // Articles of Motors category
            Article tesla = new Article("Tesla Model 3", 42999.99f, auctionEndDate, motors, batman);
            motors.addArticle(tesla);
            Article civic = new Article("Honda Civic 2021", 21999.99f, auctionEndDate, motors, superman);
            motors.addArticle(civic);
            Article f150 = new Article("Ford F-150 Pickup Truck", 33999.99f, auctionEndDate, motors, spiderman);
            motors.addArticle(f150);
            Article corvette = new Article( "Chevrolet Corvette Stingray", 59999.99f, auctionEndDate, motors, captainMarvel);
            motors.addArticle(corvette);

			// Save categories
            // Note: Articles are saved automatically due to CascadeType.ALL in Category entity
			categoryRepository.saveAll(List.of(electronics, sports, motors));			
			logger.info("Categories saved!");
		};
    }
}