import React from 'react';
import Breadcrumb from '../Common/Breadcrumb';
import Instructors from './Instructors';

const InstructorMain = () => {
    return (
        <div>
            <Breadcrumb breadcrumbTitle="Instructor" breadcrumbSubTitle="instructor" />
            <Instructors />
        </div>
    );
};

export default InstructorMain;