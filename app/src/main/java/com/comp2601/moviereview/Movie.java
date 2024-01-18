package com.comp2601.moviereview;

public class Movie {
    private final String name;
    private final String imageURL;
    private final String imdbID;

    //initialize Movie objects to display on the home page
    public static final Movie[] movies = {
            new Movie("Little Women", "https://m.media-amazon.com/images/M/MV5BY2QzYTQyYzItMzAwYi00YjZlLThjNTUtNzMyMDdkYzJiNWM4XkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_SX300.jpg", "tt3281548"),
            new Movie("Brave", "https://m.media-amazon.com/images/M/MV5BMzgwODk3ODA1NF5BMl5BanBnXkFtZTcwNjU3NjQ0Nw@@._V1_SX300.jpg", "tt1217209"),
            new Movie("Tangled Ever After", "https://m.media-amazon.com/images/M/MV5BMTgzNDAxNDg2Nl5BMl5BanBnXkFtZTcwNzMzNTIyNw@@._V1_SX300.jpg", "tt2112281"),
            new Movie("I Care a Lot", "https://m.media-amazon.com/images/M/MV5BYWU2ZTRhNDMtMWYxMC00ZTVkLThjZmItZGY4MGU0YmZlMjJlXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_SX300.jpg", "tt9893250"),
            new Movie("Thunder Force", "https://m.media-amazon.com/images/M/MV5BMWZkM2I2NjEtNWM0Mi00MTgwLWJlYTAtYmNkZWYzNmQ1ZTBiXkEyXkFqcGdeQXVyMDM2NDM2MQ@@._V1_SX300.jpg", "tt10121392"),
            new Movie("If I Stay", "https://m.media-amazon.com/images/M/MV5BMjI4NjkxODMyMF5BMl5BanBnXkFtZTgwODYwODQ5MTE@._V1_SX300.jpg", "tt1355630"),
            new Movie("La La Land", "https://m.media-amazon.com/images/M/MV5BMzUzNDM2NzM2MV5BMl5BanBnXkFtZTgwNTM3NTg4OTE@._V1_SX300.jpg", "tt3783958"),
            new Movie("The Shape of Water", "https://m.media-amazon.com/images/M/MV5BNGNiNWQ5M2MtNGI0OC00MDA2LWI5NzEtMmZiYjVjMDEyOWYzXkEyXkFqcGdeQXVyMjM4NTM5NDY@._V1_SX300.jpg", "tt5580390"),
            new Movie("Gravity", "https://m.media-amazon.com/images/M/MV5BNjE5MzYwMzYxMF5BMl5BanBnXkFtZTcwOTk4MTk0OQ@@._V1_SX300.jpg", "tt1454468"),
            new Movie("Cinderella", "https://m.media-amazon.com/images/M/MV5BMjMxODYyODEzN15BMl5BanBnXkFtZTgwMDk4OTU0MzE@._V1_SX300.jpg", "tt1661199"),
            new Movie("Snowpiercer", "https://m.media-amazon.com/images/M/MV5BMTQ3NzA1MTY3MV5BMl5BanBnXkFtZTgwNzE2Mzg5MTE@._V1_SX300.jpg", "tt1706620"),
            new Movie("Proximity", "https://m.media-amazon.com/images/M/MV5BNzFjYTFkZjItZDI1Zi00MzExLTg1ODctMWQ0ZTc2YTRhZmM4XkEyXkFqcGdeQXVyMjU1NjU1NDU@._V1_SX300.jpg", "tt8718300"),
            new Movie("Flipped", "https://m.media-amazon.com/images/M/MV5BMTU2NjQ1Nzc4MF5BMl5BanBnXkFtZTcwNTM0NDk1Mw@@._V1_SX300.jpg", "tt0817177"),
            new Movie("Regression", "https://m.media-amazon.com/images/M/MV5BODk4NWI4ZDAtZDNmYy00Mjk3LTg2NDMtNDkxMmZiMDM1OTQzXkEyXkFqcGdeQXVyNTAyNDQ2NjI@._V1_SX300.jpg", "tt3319920"),
            new Movie("Searching", "https://m.media-amazon.com/images/M/MV5BMjIwOTA3NDI3MF5BMl5BanBnXkFtZTgwNzIzMzA5NTM@._V1_SX300.jpg", "tt7668870"),
            new Movie("Passion", "https://m.media-amazon.com/images/M/MV5BMjMwOTg4NjQ0MV5BMl5BanBnXkFtZTcwMTkzMDg2OQ@@._V1_SX300.jpg", "tt1829012"),
            new Movie("Onward", "https://m.media-amazon.com/images/M/MV5BMTZlYzk3NzQtMmViYS00YWZmLTk5ZTEtNWE0NGVjM2MzYWU1XkEyXkFqcGdeQXVyNDg4NjY5OTQ@._V1_SX300.jpg", "tt7146812"),
            new Movie("Joe", "https://m.media-amazon.com/images/M/MV5BMjExMzk5MTM1Ml5BMl5BanBnXkFtZTgwNzAzODgxMTE@._V1_SX300.jpg", "tt2382396"),
            new Movie("Cake", "https://m.media-amazon.com/images/M/MV5BMmE2ZTQ5NTUtMzM2NS00YTIxLWEyMDQtMjBiMGNmODgwN2U5XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg", "tt3442006"),
            new Movie("The Good Dinosaur", "https://m.media-amazon.com/images/M/MV5BMTc5MTg2NjQ4MV5BMl5BanBnXkFtZTgwNzcxOTY5NjE@._V1_SX300.jpg", "tt1979388"),
            new Movie("Tinker Bell", "https://m.media-amazon.com/images/M/MV5BOWQzNTVmZDgtNzcwYi00YzY1LTllNzgtNjA0N2QwNTRlOTFmXkEyXkFqcGdeQXVyNDE5MTU2MDE@._V1_SX300.jpg", "tt0823671"),
            new Movie("Aladdin", "https://m.media-amazon.com/images/M/MV5BY2Q2NDI1MjUtM2Q5ZS00MTFlLWJiYWEtNTZmNjQ3OGJkZDgxXkEyXkFqcGdeQXVyNTI4MjkwNjA@._V1_SX300.jpg", "tt0103639"),
            new Movie("LOL", "https://m.media-amazon.com/images/M/MV5BMTA0MjI5ODA3MjReQTJeQWpwZ15BbWU3MDI1NTE3Njc@._V1_SX300.jpg", "tt1592873"),
            new Movie("Fallen", "https://m.media-amazon.com/images/M/MV5BY2Q1OGI4ZGQtMzc3OS00YzIyLThhNWQtYWUxODc3NzU0YWQ4L2ltYWdlL2ltYWdlXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg", "tt0119099"),
            new Movie("6 Years", "https://m.media-amazon.com/images/M/MV5BMTYzMTMwMDMzMF5BMl5BanBnXkFtZTgwNDc4MjUzNjE@._V1_SX300.jpg", "tt3799372"),
            new Movie("Flower", "https://m.media-amazon.com/images/M/MV5BYzViOTBmMGUtODBlMi00YzYzLThiM2MtOGFlMzZkNWU4NDRjXkEyXkFqcGdeQXVyODE0MDY3NzY@._V1_SX300.jpg", "tt2582784"),
            new Movie("2067", "https://m.media-amazon.com/images/M/MV5BM2RiYTM3NjAtNDUyOC00OWFhLWE3ZGEtNjkzNzI5YmE1M2E5XkEyXkFqcGdeQXVyMDM2NDM2MQ@@._V1_SX300.jpg", "tt1918734"),
            new Movie("Frozen", "https://m.media-amazon.com/images/M/MV5BMTQ1MjQwMTE5OF5BMl5BanBnXkFtZTgwNjk3MTcyMDE@._V1_SX300.jpg", "tt2294629"),
            new Movie("The BFG", "https://m.media-amazon.com/images/M/MV5BNjAzOTUzNTY3Ml5BMl5BanBnXkFtZTgwMjYwNzE5ODE@._V1_SX300.jpg", "tt3691740"),
            new Movie("Alice in Wonderland", "https://m.media-amazon.com/images/M/MV5BMTMwNjAxMTc0Nl5BMl5BanBnXkFtZTcwODc3ODk5Mg@@._V1_SX300.jpg", "tt1014759")
    };

    //constructor for Movie
    Movie(String name, String url, String imdbID){
        this.name = name;
        this.imageURL = url;
        this.imdbID = imdbID;
    }

    //get movie name
    public String getName() {
        return name;
    }

    //get image URL
    public String getImageURL(){
        return imageURL;
    }

    //get movie's imdbID
    public String getImdbID() {
        return imdbID;
    }
}
