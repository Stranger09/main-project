package com.company.mainproject;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.*;

public class ImageLoader {
    @Getter
    @Setter
    private String currentStepImageName;

    public ArrayList<String> getRenderedImages(String directoryPath, int countOfElements, int shouldBeMatched) {
        ArrayList<String> result = new ArrayList<String>() {};

        ArrayList<File> imagesFromDirectory = getImagesFromDirectory(directoryPath);
        ArrayList<File> imagesFromDirectoryLimited = new ArrayList<>(imagesFromDirectory.subList(0, countOfElements));
        Random rand = new Random();
        int stepImageIndex = rand.nextInt(imagesFromDirectoryLimited.size()-1);
        File stepFile = imagesFromDirectory.get(stepImageIndex);
        setCurrentStepImageName(stepFile.getName());

        ArrayList<Integer> randomIndexes = getRandomIndexes(shouldBeMatched, imagesFromDirectoryLimited, stepImageIndex);
        for (int i = 0; i < imagesFromDirectoryLimited.size(); i++) {

            if (imagesFromDirectoryLimited.get(i) != null) {
                if (randomIndexes.contains(i) && !imagesFromDirectoryLimited.get(i).getName().equals(stepFile.getName())) {
                    result.add(stepFile.getName());
                } else {
                    result.add(imagesFromDirectoryLimited.get(i).getName());
                }
            }
        }

        Collections.shuffle(result);

        return result;
    }

    private ArrayList<File> getImagesFromDirectory(String directoryPath) {
        File folder = new File(System.getProperty("user.dir") + directoryPath);
        ArrayList<File> list = new ArrayList<>();
        Collections.addAll(list, folder.listFiles());
        return list;
    }

    private int getDifferentIndex(ArrayList<Integer> randomIndexes, ArrayList<File> givenList, int stepImageIndex, int countOfIndexes) {
        int randomIndex = getIntNotLikeMainImageIndex(givenList, stepImageIndex);
        if (randomIndexes.contains(randomIndex) || randomIndexes.size() == countOfIndexes-1){
            return getDifferentIndex(randomIndexes, givenList, stepImageIndex, countOfIndexes);
        }
        return randomIndex;
    }

    private ArrayList<Integer> getRandomIndexes(int countOfIndexes, ArrayList<File> givenList, int stepImageIndex) {
        ArrayList<Integer> randomIndexes = new ArrayList<>();

        for (int i = 0; i < countOfIndexes-1; i++) {
            randomIndexes.add(getDifferentIndex(randomIndexes, givenList, stepImageIndex, countOfIndexes));
        }

        randomIndexes.add(stepImageIndex);

        return randomIndexes;
    }

    private int getIntNotLikeMainImageIndex(ArrayList<File> givenList, int stepImageIndex) {
        Random rand = new Random();
        int randomIndex = rand.nextInt(givenList.size()-1);
        return randomIndex == stepImageIndex? getIntNotLikeMainImageIndex(givenList, stepImageIndex) : randomIndex;
    }
}
