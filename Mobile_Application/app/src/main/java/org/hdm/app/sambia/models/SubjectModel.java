package org.hdm.app.sambia.models;

import android.media.Image;

/**
 * Data representation of a Subject.
 */
public class SubjectModel {

    /**
     * Attributes
     */
    private Image picture;
    private String name;
    private int age;
    private String gender;
    private String education;

    private String tribe;
    private int household;
    private int size;
    private int weight;

    private int landOwned;
    private int landCultivated;

    /**
     * Constructor
     */
    public SubjectModel(
            Image _picture,
            String _name,
            int _age,
            String _gender,
            String _education,
            String _tribe,
            int _household,
            int _size,
            int _weight,
            int _landOwned,
            int _landCultivated
    ) {
        this.picture = _picture;
        this.name = _name;
        this.age = _age;
        this.gender = _gender;
        this.education = _education;

        this.tribe = _tribe;
        this.household = _household;
        this.size = _size;
        this.weight = _weight;

        this.landOwned = _landOwned;
        this.landCultivated = _landCultivated;
    }

}
