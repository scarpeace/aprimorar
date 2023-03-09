import React from 'react';
import Link from 'next/link';
import Breadcrumb from '../Common/Breadcrumb';

const EventMain = () => {
    return (
        <main>
            <Breadcrumb breadcrumbTitle="Upcoming Event" breadcrumbSubTitle="Upcoming Event" />
            
            <div className="event-ara pt-120 pb-90">
                <div className="container">
                    <div className="row">
                        <div className="col-xl-8 col-lg-7">
                            <div className="single-item mb-30">
                                <div className="event_date f-left">
                                    <div className="event_date_inner">
                                    <h4>10</h4>
                                    <span>Jan 2022</span>
                                    </div>
                                </div>
                                <div className="event_info">
                                    <h3><Link href="/event-details"><a>Education Autumn Tour Conference</a></Link></h3>
                                    <div className="event-detalis d-flex align-items-center">
                                        <div className="event-time mr-30 d-flex align-items-center">
                                            <i className="flaticon-clock-1"></i>
                                            <span>10:30 AM</span>
                                        </div>
                                        <div className="event-location d-flex align-items-centere">
                                            <i className="flaticon-pin"></i>
                                            <span>Zeoyan Stadium, London</span>
                                        </div>
                                    </div>
                                    <div className="event-aduence d-flex align-items-center">
                                        <div className="aduence-thumb">
                                            <img src="assets/img/event/event-01.png" alt="event-thumb"/>
                                            <img src="assets/img/event/event-02.png" alt="event-thumb"/>
                                            <img src="assets/img/event/event-03.png" alt="event-thumb"/>
                                            <img src="assets/img/event/event-04.png" alt="event-thumb"/>
                                            <img src="assets/img/event/event-05.png" alt="event-thumb"/>
                                        </div>
                                        <div className="adence-info">
                                            <span>+55 Audience have joined</span>
                                        </div>
                                    </div>
                                </div>
                                <div className="get-ticket-btn">
                                    <Link href="/"><a className="get-btn">Get ticket</a></Link>
                                </div>
                            </div>
                        <div className="single-item mb-30">
                            <div className="event_date f-left">
                                <div className="event_date_inner">
                                <h4>18</h4>
                                <span>Jan 2022</span>
                                </div>
                            </div>
                            <div className="event_info">
                                <h3><Link href="/event-details"><a>General ideas of Land Management System</a></Link></h3>
                                <div className="event-detalis d-flex align-items-center">
                                <div className="event-time mr-30 d-flex align-items-center">
                                    <i className="flaticon-clock-1"></i>
                                    <span>10:30 AM</span>
                                </div>
                                <div className="event-location d-flex align-items-centere">
                                    <i className="flaticon-pin"></i>
                                    <span>Zeoyan Stadium, London</span>
                                </div>
                                </div>
                                <div className="event-aduence d-flex align-items-center">
                                <div className="aduence-thumb">
                                    <img src="assets/img/event/event-01.png" alt="event-thumb"/>
                                    <img src="assets/img/event/event-02.png" alt="event-thumb"/>
                                    <img src="assets/img/event/event-03.png" alt="event-thumb"/>
                                    <img src="assets/img/event/event-04.png" alt="event-thumb"/>
                                    <img src="assets/img/event/event-05.png" alt="event-thumb"/>
                                </div>
                                <div className="adence-info">
                                    <span>+55 Audience have joined</span>
                                </div>
                                </div>
                            </div>
                            <div className="get-ticket-btn">
                                <Link href="/"><a className="get-btn">Get ticket</a></Link>
                            </div>
                        </div>
                        <div className="single-item mb-30">
                            <div className="event_date f-left">
                                <div className="event_date_inner">
                                <h4>22</h4>
                                <span>Jan 2022</span>
                                </div>
                            </div>
                            <div className="event_info">
                                <h3><Link href="/event-details"><a>Discussion on Data Science PowerPoint</a></Link></h3>
                                <div className="event-detalis d-flex align-items-center">
                                <div className="event-time mr-30 d-flex align-items-center">
                                    <i className="flaticon-clock-1"></i>
                                    <span>10:30 AM</span>
                                </div>
                                <div className="event-location d-flex align-items-centere">
                                    <i className="flaticon-pin"></i>
                                    <span>Zeoyan Stadium, London</span>
                                </div>
                                </div>
                                <div className="event-aduence d-flex align-items-center">
                                <div className="aduence-thumb">
                                    <img src="assets/img/event/event-01.png" alt="event-thumb"/>
                                    <img src="assets/img/event/event-02.png" alt="event-thumb"/>
                                    <img src="assets/img/event/event-03.png" alt="event-thumb"/>
                                    <img src="assets/img/event/event-04.png" alt="event-thumb"/>
                                    <img src="assets/img/event/event-05.png" alt="event-thumb"/>
                                </div>
                                <div className="adence-info">
                                    <span>+55 Audience have joined</span>
                                </div>
                                </div>
                            </div>
                            <div className="get-ticket-btn">
                                <Link href="/"><a className="get-btn">Get ticket</a></Link>
                            </div>
                        </div>
                        <div className="single-item mb-30">
                            <div className="event_date f-left">
                                <div className="event_date_inner">
                                <h4>16</h4>
                                <span>Jan 2022</span>
                                </div>
                            </div>
                            <div className="event_info">
                                <h3><Link href="/event-details"><a>Foundations of Global Health</a></Link></h3>
                                <div className="event-detalis d-flex align-items-center">
                                <div className="event-time mr-30 d-flex align-items-center">
                                    <i className="flaticon-clock-1"></i>
                                    <span>10:30 AM</span>
                                </div>
                                <div className="event-location d-flex align-items-centere">
                                    <i className="flaticon-pin"></i>
                                    <span>Zeoyan Stadium, London</span>
                                </div>
                                </div>
                                <div className="event-aduence d-flex align-items-center">
                                <div className="aduence-thumb">
                                    <img src="assets/img/event/event-01.png" alt="event-thumb"/>
                                    <img src="assets/img/event/event-02.png" alt="event-thumb"/>
                                    <img src="assets/img/event/event-03.png" alt="event-thumb"/>
                                    <img src="assets/img/event/event-04.png" alt="event-thumb"/>
                                    <img src="assets/img/event/event-05.png" alt="event-thumb"/>
                                </div>
                                <div className="adence-info">
                                    <span>+55 Audience have joined</span>
                                </div>
                                </div>
                            </div>
                            <div className="get-ticket-btn">
                                <Link href="/"><a className="get-btn">Get ticket</a></Link>
                            </div>
                        </div>
                    </div>
                    <div className="col-xl-4 col-lg-5 col-md-8">
                        <div className="sidebar-widget-wrapper">
                            <div className="sidebar-widget mb-30">
                                <div className="event-wrapper">
                                <div className="event-select">
                                    <select>
                                        <option value="volvo">All Events</option>
                                        <option value="saab">This Month</option>
                                        <option value="opel">Next Month</option>
                                        <option value="audi">This Year</option>
                                    </select>
                                </div>
                                </div>
                            </div>
                            <div className="side-bar-widget mb-30">
                                <div className="event-sidebar">
                                <div className="find-event">
                                    <div className="find-event-info">
                                        <h4>Find Events</h4>
                                    </div>
                                    <div className="find-event-wrapper mb-25">
                                        <div className="find-event-input">
                                            <input type="date"/>
                                            <i className="flaticon-calendar"></i>
                                        </div>
                                    </div>
                                    <div className="find-event-wrapper mb-25">
                                        <div className="find-event-input">
                                            <input type="text" placeholder="Location"/>
                                            <i className="flaticon-pin-1"></i>
                                        </div>
                                    </div>
                                    <div className="find-event-wrapper mb-25">
                                        <div className="find-event-input">
                                            <input type="text" placeholder="Search keyword...."/>
                                            <i className="flaticon-search"></i>
                                        </div>
                                    </div>
                                </div>
                                <div className="zoom-btn">
                                    <a href="#" className=""></a>
                                    <Link href="/"><a className="event-btn">Find Event</a></Link>
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

export default EventMain;