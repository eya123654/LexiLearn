package com.example.demo1.Controllers;

import com.example.demo1.Entities.Cours;

@FunctionalInterface
public interface CourseDetailsRequestListener {
        void onRequestCourseDetails(Cours course);
        }