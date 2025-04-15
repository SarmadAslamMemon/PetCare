package com.example.petcare;

import android.graphics.Bitmap;

import com.example.petcare.modelclass.Blog;
import com.example.petcare.modelclass.Doctor;
import com.example.petcare.modelclass.HealthTip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeneralClass {

    // Static list of HealthTips with improved formatting
    public static List<HealthTip> getHealthTips() {
        List<HealthTip> healthTips = new ArrayList<>();

        healthTips.add(new HealthTip("Healthy Diet",
                "1. Ensure your pet eats a balanced and nutritious diet.\n \n" +
                        "2. A healthy diet is essential for maintaining your pet's energy levels.\n \n" +
                        "3. Make sure to provide a combination of proteins, vitamins, and minerals.\n \n" +
                        "4. Avoid overfeeding or giving too many treats to prevent obesity.\n \n" +
                        "5. Consult your vet to determine the best diet for your pet's breed and age.",
                "pet_care_food_lottie"));

        healthTips.add(new HealthTip("Regular Exercise",
                "1. Daily exercise keeps your pet healthy and happy.\n \n" +
                        "2. Exercise helps maintain a healthy weight and improves joint mobility.\n \n" +
                        "3. Take your dog on walks, play with your cat, or let your pet run in a safe area.\n \n" +
                        "4. Interactive toys like balls and frisbees are great for outdoor play.\n \n" +
                        "5. Remember, different pets need different types of exercises, so tailor it accordingly.",
                "dog_walking_anim"));

        healthTips.add(new HealthTip("Grooming",
                "1. Regular grooming helps your pet stay clean and comfortable.\n \n" +
                        "2. Brushing your pet’s coat prevents tangles and matting, especially in long-haired pets.\n \n" +
                        "3. Nail trimming is also important to avoid overgrowth and discomfort.\n \n" +
                        "4. Bathing your pet regularly ensures their skin remains healthy and clean.\n \n" +
                        "5. Don’t forget to clean your pet's ears, teeth, and paws as part of grooming.",
                "dogs_smiling_anim"));

        healthTips.add(new HealthTip("Regular Vet Visits",
                "1. Schedule regular vet check-ups for your pet's health.\n \n" +
                        "2. Routine visits can catch health problems early before they become serious.\n \n" +
                        "3. Ensure your pet is up-to-date on vaccinations and parasite prevention.\n \n" +
                        "4. Annual health exams also provide an opportunity to discuss any concerns with your vet.\n \n" +
                        "5. Healthy pets live longer and happier lives, so regular check-ups are essential.",
                "lottie_vet"));

        return healthTips;
    }

    // Example of getting pet types
    public static String[] getPetTypes() {
        return new String[]{"Dog", "Cat", "Bird", "Fish", "Rabbit"};
    }


//        * Get a static list of doctors.
//     *
//             * @return List of Doctor objects
//     */
public static List<Doctor> getDoctors() {
    List<Doctor> doctors = new ArrayList<>();

    doctors.add(new Doctor("Dr. Ahmed Khan", "DVM", "Pet Care Veterinary Clinic, Gulshan-e-Iqbal", "+92 321-2345678",R.drawable.doc_male3,"Specialized in dogs and cats,Take a good care of them "));
    doctors.add(new Doctor("Dr. Fatima Siddiqui", "DVM", "The Animal Care Clinic, Gulistan-e-Johar", "+92 322-3456789",R.drawable.doc_fem_2,"Specialized in birds,Take a good care of them "));
    doctors.add(new Doctor("Dr. Muhammad Usman", "DVM", "Karachi Animal Hospital, Gulshan-e-Iqbal", "+92 300-4567890",R.drawable.doc_male_1,"Specialized in fish,Take a good care of them"));
    doctors.add(new Doctor("Dr. Ayesha Farooq", "DVM", "Companion Animal Clinic, Gulistan-e-Johar", "+92 331-5678901",R.drawable.doc_fem_1,"Specialized in rabbits,Take a good care of them"));
    doctors.add(new Doctor("Dr. Zainab Iqbal", "DVM", "Happy Tails Veterinary Clinic, Gulshan-e-Iqbal", "+92 315-6789012",R.drawable.doc_fem_3,"Specialized in dogs,Take a good care of them"));

    return Collections.unmodifiableList(doctors);
}


    public static List<Blog> getBlogs() {
        List<Blog> blogs = new ArrayList<>();

        // Adding static blogs to the list with HTML-formatted content
        blogs.add(new Blog(1, 1, 1, "Best Meal Times for Pets",
                "<b>As a pet owner, it’s essential to establish a consistent feeding routine for your pets.</b> This helps maintain their health, regulates their digestion, and establishes a sense of security. Here’s why meal timing matters for pets:<br>" +
                        "- <b>Establishes a routine:</b> Pets thrive on routines, and regular meal times help create a predictable environment.<br>" +
                        "- <b>Supports digestion:</b> Feeding your pets at the same time each day helps regulate their digestive system and reduce gastrointestinal issues.<br>" +
                        "- <b>Energy levels:</b> Pets need energy throughout the day, and meal times ensure they have enough fuel for activity.<br>" +
                        "- <b>Best times to feed pets:</b> <br>" +
                        "    - <b>Dogs:</b> Ideally fed in the morning and evening to prevent long periods of hunger. It also helps with digestion and reduces the chance of obesity.<br>" +
                        "    - <b>Cats:</b> Cats prefer smaller meals throughout the day. You can feed them several times per day or leave food out for them to graze.<br>" +
                        "- <b>Avoid feeding before bedtime:</b> Feeding your pets right before they go to sleep can cause digestive discomfort, bloating, or even vomiting.<br>" +
                        "- <b>Fresh water is essential:</b> Always ensure that your pets have access to fresh water, especially between meals.<br>" +
                        "By maintaining these meal timings, you’re helping your pet stay healthier, happier, and more energetic throughout their day."));

        blogs.add(new Blog(2, 2, 2, "Healthy Food for Birds",
                "<b>Birds have unique dietary needs, and it's crucial to feed them a balanced diet to ensure they stay healthy and vibrant.</b> Here are some key tips on feeding birds:<br>" +
                        "- <b>Diverse diet for optimal health:</b> Birds need a mix of seeds, fruits, vegetables, and protein to stay healthy. A varied diet prevents boredom and supports mental stimulation.<br>" +
                        "- <b>Types of food to offer:</b><br>" +
                        "    - <b>Seeds:</b> Sunflower seeds, safflower, and millet are good for most birds but should be fed in moderation.<br>" +
                        "    - <b>Fruits:</b> Offer fruits like apples, bananas, oranges, grapes, and berries. Make sure to remove any seeds from fruits like apples or peaches.<br>" +
                        "    - <b>Vegetables:</b> Leafy greens (spinach, kale, collard greens) are excellent. You can also feed them broccoli, carrots, and bell peppers.<br>" +
                        "    - <b>Proteins:</b> Birds also need protein to stay strong. You can give them small amounts of cooked eggs, beans, or tofu.<br>" +
                        "- <b>Avoid harmful foods:</b> Some foods are toxic to birds. Never feed birds chocolate, avocado, alcohol, caffeine, or high-fat foods like fried foods.<br>" +
                        "- <b>Fresh water:</b> Birds need access to clean, fresh water at all times. Make sure to change their water daily.<br>" +
                        "- <b>Grain-based food:</b> Birds love grain-based foods like whole grains and oat flakes. These foods are rich in fiber and help with digestion.<br>" +
                        "A balanced diet keeps your bird active, happy, and healthy, and provides them with the necessary nutrients for a long life."));

        blogs.add(new Blog(3, 3, 3, "Most Common Diseases in Cats",
                "<b>Cats are generally independent and resilient, but they are prone to certain diseases.</b> Early detection and regular veterinary care can help prevent or manage these conditions. Below are some of the most common diseases in cats:<br>" +
                        "- <b>Feline Upper Respiratory Infection (URI):</b> A viral or bacterial infection that affects a cat’s nose, sinuses, and throat. Symptoms include sneezing, coughing, nasal discharge, and loss of appetite.<br>" +
                        "- <b>Feline Leukemia Virus (FeLV):</b> A serious viral disease that weakens the immune system, leaving the cat vulnerable to other infections. FeLV is transmitted through saliva, blood, or urine, and it can cause lethargy, weight loss, and anemia.<br>" +
                        "- <b>Chronic Kidney Disease (CKD):</b> A common disease in older cats, where the kidneys gradually lose function. Symptoms include excessive thirst, weight loss, vomiting, and a decrease in appetite.<br>" +
                        "- <b>Hyperthyroidism:</b> A condition in which the thyroid gland produces excessive hormones, leading to weight loss, increased appetite, and hyperactivity.<br>" +
                        "- <b>Dental Disease:</b> Cats can suffer from dental issues, including gum disease and tooth decay, leading to bad breath, drooling, and difficulty eating.<br>" +
                        "- <b>Arthritis:</b> Common in older cats, arthritis causes joint pain, stiffness, and difficulty jumping or climbing.<br>" +
                        "Preventing these diseases includes vaccinations, regular vet checkups, a balanced diet, and ensuring your cat is kept indoors to avoid infections. Regular checkups with your vet can help diagnose problems early, improving the chances of successful treatment."));

        blogs.add(new Blog(4, 4, 4, "Pet Care Health Tip: Regular Exercise for Dogs",
                "<b>Physical activity is essential for dogs to maintain a healthy weight, reduce stress, and prevent behavioral issues.</b> Here's why regular exercise is vital for dogs and how you can keep them active:<br>" +
                        "- <b>Prevents obesity:</b> Without proper exercise, dogs can easily become overweight, which increases the risk of serious health problems like heart disease, diabetes, and joint issues.<br>" +
                        "- <b>Improves behavior:</b> Dogs that don’t get enough exercise can become anxious, destructive, or hyperactive. Regular walks or playtime help them release excess energy, leading to a calmer, happier dog.<br>" +
                        "- <b>Strengthens muscles and joints:</b> Regular exercise keeps your dog’s muscles strong and improves their flexibility, reducing the risk of arthritis and joint pain.<br>" +
                        "- <b>Mental stimulation:</b> Exercise isn’t just about physical activity; it’s also about engaging their minds. Walking in new areas, playing games like fetch, or using interactive toys helps mentally stimulate your dog.<br>" +
                        "- <b>How much exercise does your dog need?</b><br>" +
                        "    - <b>Small dogs:</b> Require about 30 minutes to an hour of exercise per day.<br>" +
                        "    - <b>Large or high-energy dogs:</b> May need an hour or more of exercise, including vigorous activities like running or playing.<br>" +
                        "- <b>Types of exercise:</b> Consider walks, jogs, hikes, fetch, tug-of-war, and dog sports. Agility training or swimming are great options too.<br>" +
                        "Ensuring your dog gets enough exercise every day will keep them healthy, fit, and mentally stimulated. Regular exercise also strengthens the bond between you and your pet, promoting a positive relationship."));

        return blogs;
    }

}
