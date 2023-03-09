import React from 'react';
import Link from 'next/link';

const EventDetailsSidebar = () => {
    return (
        <div className="sidebar-widget-wrapper">
            <div className="event-speaker-wrapper mb-30">
                <div className="event-speaker-info">
                <h4>Speaker</h4>
                </div>
                <div className="event-sidebar-thumb w-img">
                    <Link href="/instructor"><a><img src="assets/img/event/speaker.png" alt="Speaker"/></a></Link>
                </div>
                <div className="event-speaker-content text-center">
                <span><Link href="/instructor"><a>Crish Denial</a></Link></span>
                <p>Motivational Speaker</p>
                </div>
            </div>
            <div className="event-information-wrapper mb-30">
                <div className="event-price-info">
                <div className="event-ticket-cost">
                    <span>Ticket Costs:</span>
                </div>
                <div className="event-price">
                    <span>$24.00</span>
                    <div className="old-price">
                        <del>$48.00</del>
                    </div>
                </div>
                </div>
                <div className="event-information-list">
                <ul>
                    <li>
                        <div className="information-list">
                            <i className="flaticon-calendar"></i>
                            <span>Date</span>
                        </div>
                        <div className="information-list">
                            <span>10 Jan 2022</span>
                        </div>
                    </li>
                    <li>
                        <div className="information-list">
                            <i className="flaticon-clock"></i>
                            <span>Schedule</span>
                        </div>
                        <div className="information-list">
                            <span>10 AM - 12 PM</span>
                        </div>
                    </li>
                    <li>
                        <div className="information-list">
                            <i className="flaticon-place"></i>
                            <span>Location</span>
                        </div>
                        <div className="information-list">
                            <span>New York, TX 82760, US</span>
                        </div>
                    </li>
                    <li>
                        <div className="information-list">
                            <i className="flaticon-menu-2"></i>
                            <span>Category</span>
                        </div>
                        <div className="information-list">
                            <span>Business</span>
                        </div>
                    </li>
                    <li>
                        <div className="information-list">
                            <i className="flaticon-global"></i>
                            <span>Laguage</span>
                        </div>
                        <div className="information-list">
                            <span>English</span>
                        </div>
                    </li>
                    <li>
                        <div className="information-list">
                            <i className="flaticon-bookmark-white"></i>
                            <span>Estimated Seat</span>
                        </div>
                        <div className="information-list">
                            <span>470 Seats</span>
                        </div>
                    </li>
                </ul>
                </div>
                <Link href="/contact"><a className="event-btn">Join this Event</a></Link>
            </div>
            <div className="event-sponsor-wrapper mb-30">
                <div className="sopnsor-tittle">
                <h4>Sponsor by</h4>
                </div>
                <div className="sponsor-thumb">
                <a href="#"> <img src="assets/img/logo/sopnsor-logo-1.png" alt="sponsor-logo"/></a>
                <a href="#"> <img src="assets/img/logo/sopnsor-logo-2.png" alt="sponsor-logo"/></a>
                </div>
            </div>
        </div>
    );
};

export default EventDetailsSidebar;