import React from 'react';
import Link from 'next/link';

const AcademicCourse = () => {
    return (
        <div className="academic-courses-area p-relative pt-110 pb-120">
        <img className="academic-shape-2" src="assets/img/shape/acadenic-shape-2.png" alt="shape"/>
        <div className="container">
            <div className="row justify-content-center">
                <div className="col-xl-4 col-lg-4">
                    <div className="section-title mb-50">
                        <h2>Academic <span className="down-mark-line-2">Courses</span></h2>
                    </div>
                </div>
            </div>
            <div className="row">
                <div className="col-xl-4 col-lg-6 col-md-6">
                    <div className="academic-box position-relative mb-30">
                        <img className="academic-shape" src="assets/img/shape/acadenic-shape-1.png" alt="image not found"/>
                        <div className="academic-thumb">
                            <img src="assets/img/course/academi-course-2.jpg" alt="image not found"/>
                        </div>
                        <div className="academic-content">
                            <div className="academic-content-header">
                                <Link href="/course-details"><a><h3>Bachelor of Business Administration ( Accounting)</h3></a></Link>
                            </div>
                            <div className="academic-body">
                                <div className="academic-tutor d-flex align-items-center">
                                    <img src="assets/img/course/academic-tutor-1.png" alt="image not found"/>
                                    <Link href="/instructor-profile"><a>Professor David</a></Link>
                                </div>
                                <p>Helping employees gain skills and development often take a back seat to business
                                </p>
                            </div>
                            <div className="academic-footer">
                                <div className="coursee-clock">
                                    <i className="flaticon-wall-clock"></i><span>4 Years</span>
                                </div>
                                <div className="course-creadit">
                                    <i className="flaticon-menu-1"></i><span>8 Credit</span>
                                </div>
                                <Link href="/course-details"><a className="edo-course-sec-btn">Apply now</a></Link>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="col-xl-4 col-lg-6 col-md-6">
                    <div className="academic-box mb-30">
                        <div className="academic-thumb">
                            <img src="assets/img/course/academi-course-3.jpg" alt="image not found"/>
                        </div>
                        <div className="academic-content">
                            <div className="academic-content-header">
                                <Link href="/course-details"><a><h3>Entrepreneurship Essentials Business Course</h3></a></Link>
                            </div>
                            <div className="academic-body">
                                <div className="academic-tutor d-flex align-items-center">
                                    <img src="assets/img/course/academic-tutor-2.png" alt="image not found"/>
                                    <Link href="/instructor-profile"><a>Professor Watson</a></Link>
                                </div>
                                <p>Helping employees gain skills and development often take a back seat to business
                                </p>
                            </div>
                            <div className="academic-footer">
                                <div className="coursee-clock">
                                    <i className="flaticon-wall-clock"></i><span>4 Years</span>
                                </div>
                                <div className="course-creadit">
                                    <i className="flaticon-menu-1"></i><span>8 Credit</span>
                                </div>
                                <Link href="/course-details"><a className="edo-course-sec-btn">Apply now</a></Link>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="col-xl-4 col-lg-6 col-md-6">
                    <div className="academic-box mb-30">
                        <div className="academic-thumb">
                            <img src="assets/img/course/academi-course-4.jpg" alt="image not found"/>
                        </div>
                        <div className="academic-content">
                            <div className="academic-content-header">
                                <Link href="/course-details"><a><h3>Financial Analysis Valuation for Lawyers Eduman</h3></a></Link>
                            </div>
                            <div className="academic-body">
                                <div className="academic-tutor d-flex align-items-center">
                                    <img src="assets/img/course/academic-tutor-3.png" alt="image not found"/>
                                    <Link href="/instructor-profile"><a>Professor Michel</a></Link>
                                </div>
                                <p>Helping employees gain skills and development often take a back seat to business
                                </p>
                            </div>
                            <div className="academic-footer">
                                <div className="coursee-clock">
                                    <i className="flaticon-wall-clock"></i><span>4 Years</span>
                                </div>
                                <div className="course-creadit">
                                    <i className="flaticon-menu-1"></i><span>8 Credit</span>
                                </div>
                                <Link href="/course-details"><a className="edo-course-sec-btn">Apply now</a></Link>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div className="row justify-content-center">
                <div className="col-xl-3 text-center">
                    <div className="academic-bottom-btn ">
                        <Link href="/course"><a className="edo-theme-btn mt-30">View all course</a></Link>
                    </div>
                </div>
            </div>
        </div>
    </div>
    );
};

export default AcademicCourse;