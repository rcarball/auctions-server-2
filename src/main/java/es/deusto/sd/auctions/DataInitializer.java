/**
 * Initially generated with ChatGPT 4o and adapted using GitHub Copilot.
 * Reviewed and updated with assistance from Claude Opus 4.8 (July 2026)
 * and ChatGPT-6.1 Sol (October 2026).
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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import es.deusto.sd.auctions.dao.ArticleRepository;
import es.deusto.sd.auctions.dao.CategoryRepository;
import es.deusto.sd.auctions.dao.UserRepository;
import es.deusto.sd.auctions.entity.Article;
import es.deusto.sd.auctions.entity.Category;
import es.deusto.sd.auctions.entity.User;

@Configuration
public class DataInitializer {

	private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final TransactionTemplate transactionTemplate;

    public DataInitializer(PlatformTransactionManager transactionManager) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }
	
    @Bean
    CommandLineRunner initData(CategoryRepository categoryRepository, UserRepository userRepository, ArticleRepository articleRepository) {
		// The runner executes after bean creation; its insertions need their own transaction.
		return args -> transactionTemplate.executeWithoutResult(status -> {
			// Database is already initialized
            if (categoryRepository.count() > 0) {                
                return;
            }			
			
			// Create some users.
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
            Article iphone = new Article("Apple iPhone 16 Pro", 1099.99, auctionEndDate, electronics, batman);
            electronics.addArticle(iphone);
            Article ps5 = new Article("Sony PlayStation 5 Pro", 699.99, auctionEndDate, electronics, spiderman);
            electronics.addArticle(ps5);
            Article macbook = new Article("MacBook Air M3", 1099.99, auctionEndDate, electronics, wonderWoman);
            electronics.addArticle(macbook);
            Article samsung = new Article("Samsung Galaxy S24 Ultra", 1299.99, auctionEndDate, electronics, captainMarvel);
            electronics.addArticle(samsung);
            // Articles of Sporting Goods category
            Article tennisRacket = new Article("Wilson Blade 98 V9 Tennis Racket", 249.99, auctionEndDate, sports, batman);
            sports.addArticle(tennisRacket);
            Article soccerBall = new Article("Adidas Fussballliebe Pro Ball", 149.99, auctionEndDate, sports, blackWidow);
            sports.addArticle(soccerBall);
            Article fitbit = new Article("Fitbit Charge 6 Fitness Tracker", 159.99, auctionEndDate, sports, captainMarvel);
            sports.addArticle(fitbit);
            Article peloton = new Article("Peloton Bike+", 2495.00, auctionEndDate, sports, wonderWoman);
            sports.addArticle(peloton);
            // Articles of Motors category
            Article c3 = new Article("Citroën C3 1.2 PureTech Feel (2020)", 10890.00, auctionEndDate, motors, batman);
            motors.addArticle(c3);
            Article mgZs = new Article("MG ZS 1.5 VTI-tech Comfort (2022)", 13990.00, auctionEndDate, motors, superman);
            motors.addArticle(mgZs);
            Article fiat500 = new Article("Fiat 500 1.0 Hybrid Cult ECO (2021)", 9950.00, auctionEndDate, motors, spiderman);
            motors.addArticle(fiat500);
            Article tucson = new Article("Hyundai Tucson 1.6 TGDI Maxx (2021)", 20900.00, auctionEndDate, motors, captainMarvel);
            motors.addArticle(tucson);

			// Save categories
            // Note: Articles are saved automatically due to CascadeType.ALL in Category entity
			categoryRepository.saveAll(List.of(electronics, sports, motors));			
			logger.info("Categories saved!");
		});
    }
}
