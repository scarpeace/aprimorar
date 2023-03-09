import React, { useState } from 'react';
import Calendar from 'react-calendar';
import Breadcrumb from '../Common/Breadcrumb';
import 'react-calendar/dist/Calendar.css';
import Link from 'next/link';


const ZoomClassDetailsMain = () => {
    const [value, onChange] = useState(new Date());

    return (
        <main>
            <Breadcrumb breadcrumbTitle="Startup Business Management Live Classn" breadcrumbSubTitle="Startup Business Management Live Classn" />

            <div className="zoom-class-detalis-area pt-120 pb-85">
                <div className="container">
                    <div className="row">
                        <div className="col-xl-8 col-lg-7">
                            <div className="zoom-main-thumb w-img mb-30">
                                <img src="assets/img/zoom/zoom-main-thumb.jpg" alt="zoom-tumb"/>
                            </div>
                            <div className="zoom-main-content">
                                <div className="content-main-heading">
                                    <a className="category-color category-color-3" href="#">Business</a>
                                    <h2>Startup Business Management Live Class</h2>
                                </div>
                                <div className="contents-widget">
                                    <h4>Introduction</h4>
                                    <p>There are so many websites out there that have not considered the overall usability of their
                                    visually impaired users. When it comes to designing better links and sending better emails,
                                    Slava Shestopalov has a few tips on how to improve your websites experience while
                                    accessibility in mind.</p>
                                </div>
                                <div className="contents-widget">
                                    <h4>Objective:</h4>
                                    <div className="objective-list">
                                    <ul>
                                        <li>Exercise</li>
                                        <li>Case Study</li>
                                        <li>Role Play/ Simulation</li>
                                        <li>Lecture Discussion and </li>
                                        <li>Sharing/ Participatory</li>
                                    </ul>
                                    </div>
                                </div>
                                <div className="contents-widget mb-30">
                                    <h4>Contents of Class:</h4>
                                    <ul>
                                    <li>• HR Audit: Concept</li>
                                    <li>• Objectives and Purpose of Audit</li>
                                    <li>• Principles of Effective HR Auditing</li>
                                    <li>• Principles of Effective HR Auditing</li>
                                    <li>• Knowledge, Skills and Attitude of an HR Auditor</li>
                                    <li>• Tasks of an HR Auditor</li>
                                    <li>• Types of HR Audit</li>
                                    <li>• HR Audit Tools</li>
                                    <li>• Steps/ Stages of HR Audit</li>
                                    <li>• Techniques of Auditing</li>
                                    <li>• Method of HR Audit</li>
                                    <li>• Documents for HR Audit</li>
                                    <li>• HR Audit Check List</li>
                                    <li>• Specific Task during HR Audit</li>
                                    <li>• HR Audit Report Preparation</li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div className="col-xl-4 col-lg-5">
                            <div className="sidebar-widget-wrapper">
                                <div className="sidebar-widget mb-30">
                                    <div className="sidebar-widget-details">
                                    <div className="zoom-widget-tittle">
                                        <h4>Details</h4>
                                    </div>
                                    <div className="zoom-widget-list">
                                        <ul>
                                            <li>
                                                <div className="widget-detalis">
                                                <i className="flaticon-avatar"></i>
                                                <span>Hosted by</span>
                                                </div>
                                                <div className="widget-list">
                                                <span>Junior Lucy</span>
                                                </div>
                                            </li>
                                            <li>
                                                <div className="widget-detalis">
                                                <i className="flaticon-calendar"></i>
                                                <span>Date</span>
                                                </div>
                                                <div className="widget-list">
                                                <span>10 Jan 2022</span>
                                                </div>
                                            </li>
                                            <li>
                                                <div className="widget-detalis">
                                                <i className="flaticon-clock"></i>
                                                <span>Schedule</span>
                                                </div>
                                                <div className="widget-list">
                                                <span>10 AM - 12 PM</span>
                                                </div>
                                            </li>
                                            <li>
                                                <div className="widget-detalis">
                                                <i className="flaticon-video-marketing"></i>
                                                <span>Length</span>
                                                </div>
                                                <div className="widget-list">
                                                <span>2h 10m</span>
                                                </div>
                                            </li>
                                            <li>
                                                <div className="widget-detalis">
                                                <i className="flaticon-menu-2"></i>
                                                <span>Category</span>
                                                </div>
                                                <div className="widget-list">
                                                <span>Data Science</span>
                                                </div>
                                            </li>
                                            <li>
                                                <div className="widget-detalis">
                                                <i className="flaticon-earth-grid-select-language-button"></i>
                                                <span>Laguage</span>
                                                </div>
                                                <div className="widget-list">
                                                <span>English</span>
                                                </div>
                                            </li>
                                            <li>
                                                <div className="widget-detalis">
                                                <i className="flaticon-bookmark-white"></i>
                                                <span>Hosted by</span>
                                                </div>
                                                <div className="widget-list">
                                                <span>Via Zoom</span>
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                    <div className="zoom-btn">
                                        <Link href="/zoom-class-details"><a className="event-btn"><i className="flaticon-video-camera"></i>Join this class</a></Link>
                                    </div>
                                    </div>
                                </div>
                                <div className="sidebar-widget mb-30">
                                    <div className="sidebar-widget-inner">
                                    <div className="sidebar-widget-head mb-25">
                                        <h3>Find Class</h3>
                                    </div>
                                    <div className="sidebar-content">
                                        <div className="myCalendar edu-calender">
                                            <Calendar onChange={onChange} value={value} />
                                        </div>
                                    </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    );
};

export default ZoomClassDetailsMain;