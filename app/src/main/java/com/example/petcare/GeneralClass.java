package com.example.petcare;

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

    doctors.add(new Doctor("Dr. Ahmed Khan", "DVM", "Pet Care Veterinary Clinic, Gulshan-e-Iqbal", "+92 321-2345678"));
    doctors.add(new Doctor("Dr. Fatima Siddiqui", "DVM", "The Animal Care Clinic, Gulistan-e-Johar", "+92 322-3456789"));
    doctors.add(new Doctor("Dr. Muhammad Usman", "DVM", "Karachi Animal Hospital, Gulshan-e-Iqbal", "+92 300-4567890"));
    doctors.add(new Doctor("Dr. Ayesha Farooq", "DVM", "Companion Animal Clinic, Gulistan-e-Johar", "+92 331-5678901"));
    doctors.add(new Doctor("Dr. Zainab Iqbal", "DVM", "Happy Tails Veterinary Clinic, Gulshan-e-Iqbal", "+92 315-6789012"));

    return Collections.unmodifiableList(doctors);
}

}
