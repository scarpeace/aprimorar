import React from 'react';
import Link from 'next/link';

const CourseTabTwo = () => {
    return (
        <div className="student-course-area pt-110 pb-90">
            <div className="container">
                <div className="row">
                    <div className="col-xl-5 col-lg-5 f-left">
                        <div className="section-title mb-50">
                            <h2>Pick Your <br/>
                                <span className="down-mark-line-2">Professional</span> Course
                            </h2>
                        </div>
                    </div>
                    <div className="col-xl-7 col-lg-7">
                        <div className="portfolio-button mt-60">
                            <nav>
                                <div className="nav portfolio-button-tabs" id="nav-tab" role="tablist">
                                    <button className="nav-link active" id="nav-home-tab" data-bs-toggle="tab" data-bs-target="#nav-home" type="button" role="tab" aria-controls="nav-home" aria-selected="true">View All<span className="port-red">[06]</span></button>
                                    <button className="nav-link" id="nav-profile-tab" data-bs-toggle="tab" data-bs-target="#nav-profile" type="button" role="tab" aria-controls="nav-profile" aria-selected="false">Data Science<span className="port-red">[01]</span></button>
                                    <button className="nav-link" id="nav-contact-tab" data-bs-toggle="tab" data-bs-target="#nav-contact" type="button" role="tab" aria-controls="nav-contact" aria-selected="false">Development<span className="port-red">[03]</span></button>
                                    <button className="nav-link" id="nav-contact-tabA" data-bs-toggle="tab" data-bs-target="#nav-contactA" type="button" role="tab" aria-controls="nav-contactA" aria-selected="false">Business<span className="port-red">[01]</span></button>
                                    <button className="nav-link" id="nav-contact-tabB" data-bs-toggle="tab" data-bs-target="#nav-contactB" type="button" role="tab" aria-controls="nav-contactB" aria-selected="false">Life Styles<span className="port-red">[01]</span></button>
                                </div>
                            </nav>
                        </div>
                    </div>
                </div>
                <div className="course-main-items">
                    <div className="tab-content portfolio-tabs-content" id="nav-tabContent">
                        <div className="tab-pane fade show active" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab">
                            <div className='row'>
                                <div className="col-xl-3 col-lg-4 col-md-6  grid-item c-2">
                                    <div className="course-wrapper-2 mb-30">
                                        <div className="student-course-img">
                                            <img src="assets/img/student-choose/student-chourse-1.jpg" alt="image not found"/>
                                        </div>
                                        <div className="course-cart">
                                            <div className="course-info-wrapper">
                                                <div className="cart-info-body">
                                                    <span className="category-color category-color-1"><Link href="/course"><a>Development</a></Link></span>
                                                    <Link href="/course-details"><a><h3>WordPress Development Course for Plugins and Themes</h3></a></Link>
                                                    <div className="cart-lavel">
                                                        <h5>Level : <span>Beginner</span></h5>
                                                        <p>Knowledge is power. Information is liberating. Education is the premise
                                                            of progress, in every society, in every family</p>
                                                    </div>
                                                    <div className="info-cart-text">
                                                        <ul>
                                                            <li><i className="far fa-check"></i>Scratch to HTML</li>
                                                            <li><i className="far fa-check"></i>Learn how to code in Python</li>
                                                            <li><i className="far fa-check"></i>Unlimited backend database creation</li>
                                                            <li><i className="far fa-check"></i>Adobe XD Tutorials</li>
                                                        </ul>
                                                    </div>
                                                    <div className="course-action">
                                                        <Link href="/course-details"><a className="view-details-btn">View Details</a></Link>
                                                        <button className="wishlist-btn"><i className="flaticon-like"></i></button>
                                                        <Link href="/course-details"><a className="c-share-btn"><i className="flaticon-previous"></i></a></Link>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="student-course-footer">
                                            <div className="student-course-linkter">
                                                <div className="course-lessons">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                                        viewBox="0 0 16.471 16.471">
                                                        <g id="blackboard-8" transform="translate(-0.008)">
                                                            <path id="Path_106" data-name="Path 101"
                                                                d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                                fill="#575757" />
                                                        </g>
                                                    </svg>
                                                    <span className="ms-2">12 Lessons</span>
                                                </div>
                                                <div className="portfolio-price">
                                                    <span>$12.57</span>
                                                    <del>$24.50</del>
                                                </div>
                                            </div>
                                            <div className="student-course-text">
                                                <h3><Link href="/course-details"><a>WordPress Development Course for Plugins Themes</a></Link></h3>
                                            </div>
                                            <div className="portfolio-user">
                                                <div className="user-icon">
                                                    <Link href="/instructor-profile"><a><span>Danial</span></a></Link>
                                                </div>
                                                <div className="course-icon">
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <span>4.9(25)</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-xl-3 col-lg-4 col-md-6  grid-item c-3">
                                    <div className="course-wrapper-2 mb-30">
                                        <div className="student-course-img">
                                            <img src="assets/img/student-choose/student-chourse-2.jpg" alt="image not found"/>
                                        </div>
                                        <div className="course-cart">
                                            <div className="course-info-wrapper">
                                                <div className="cart-info-body">
                                                    <span className="category-color category-color-3"><Link href="/course"><a>Business</a></Link></span>
                                                    <Link href="/course-details"><a><h3>Write Better Emails: Tactics for Smarter Team Communication</h3></a></Link>
                                                    <div className="cart-lavel">
                                                        <p>Level : <span>Beginner</span></p>
                                                        <p>Knowledge is power. Information is liberating. Education is the premise
                                                            of progress, in every society, in every family</p>
                                                    </div>
                                                    <div className="info-cart-text">
                                                        <ul>
                                                            <li><i className="far fa-check"></i>Scratch to HTML</li>
                                                            <li><i className="far fa-check"></i>Learn how to code in Python</li>
                                                            <li><i className="far fa-check"></i>Unlimited backend database creation</li>
                                                            <li><i className="far fa-check"></i>Adobe XD Tutorials</li>
                                                        </ul>
                                                    </div>
                                                    <div className="course-action">
                                                        <Link href="/course-details"><a className="view-details-btn">View Details</a></Link>
                                                        <button className="wishlist-btn"><i className="flaticon-like"></i></button>
                                                        <Link href="/course-details"><a className="c-share-btn"><i className="flaticon-previous"></i></a></Link>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="student-course-footer">
                                            <div className="student-course-linkter">
                                                <div className="course-lessons">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                                        viewBox="0 0 16.471 16.471">
                                                        <g id="blackboard-1" transform="translate(-0.008)">
                                                            <path id="Path_102" data-name="Path 101"
                                                                d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                                fill="#575757" />
                                                        </g>
                                                    </svg>
                                                    <span className="ms-2">12 Lessons</span>
                                                </div>
                                                <div className="portfolio-price">
                                                    <span>$12.57</span>
                                                    <del>$24.50</del>
                                                </div>
                                            </div>
                                            <div className="student-course-text">
                                                <h3><Link href="/course-details"><a>Write Better Emails: Tactics for Smarter Team Communication</a></Link></h3>
                                            </div>
                                            <div className="portfolio-user">
                                                <div className="user-icon">
                                                    <Link href="/instructor-profile"><a><span>Dcosta</span></a></Link>
                                                </div>
                                                <div className="course-icon">
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <span>4.9(25)</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-xl-3 col-lg-4 col-md-6  grid-item c-3">
                                    <div className="course-wrapper-2 mb-30">
                                        <div className="student-course-img">
                                            <img src="assets/img/student-choose/student-chourse-3.jpg" alt="image not found"/>
                                        </div>
                                        <div className="course-cart">
                                            <div className="course-info-wrapper">
                                                <div className="cart-info-body">
                                                    <span className="category-color category-color-1"><Link href="/course"><a>Development</a></Link></span>
                                                    <Link href="/course-details"><a><h3>Master Google Docs: Free online documents for personal use</h3></a></Link>
                                                    <div className="cart-lavel">
                                                        <h5>Level : <span>Beginner</span></h5>
                                                        <p>Knowledge is power. Information is liberating. Education is the premise
                                                            of progress, in every society, in every family</p>
                                                    </div>
                                                    <div className="info-cart-text">
                                                        <ul>
                                                            <li><i className="far fa-check"></i>Scratch to HTML</li>
                                                            <li><i className="far fa-check"></i>Learn how to code in Python</li>
                                                            <li><i className="far fa-check"></i>Unlimited backend database creation</li>
                                                            <li><i className="far fa-check"></i>Adobe XD Tutorials</li>
                                                        </ul>
                                                    </div>
                                                    <div className="course-action">
                                                        <Link href="/course-details"><a className="view-details-btn">View Details</a></Link>
                                                        <button className="wishlist-btn"><i className="flaticon-like"></i></button>
                                                        <Link href="/course-details"><a className="c-share-btn"><i className="flaticon-previous"></i></a></Link>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="student-course-footer">
                                            <div className="student-course-linkter">
                                                <div className="course-lessons">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                                        viewBox="0 0 16.471 16.471">
                                                        <g id="blackboard-2" transform="translate(-0.008)">
                                                            <path id="Path_103" data-name="Path 101"
                                                                d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                                fill="#575757" />
                                                        </g>
                                                    </svg>
                                                    <span className="ms-2">12 Lessons</span>
                                                </div>
                                                <div className="portfolio-price">
                                                    <span>$12.57</span>
                                                    <del>$24.50</del>
                                                </div>
                                            </div>
                                            <div className="student-course-text">
                                                <h3><Link href="/course-details"><a>Master Google Docs: Free online documents for personal use</a></Link></h3>
                                            </div>
                                            <div className="portfolio-user">
                                                <div className="user-icon">
                                                    <Link href="/instructor-profile"><a><span>Mark</span></a></Link>
                                                </div>
                                                <div className="course-icon">
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <span>4.9(25)</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-xl-3 col-lg-4 col-md-6  grid-item c-2">
                                    <div className="course-wrapper-2 mb-30">
                                        <div className="student-course-img">
                                            <img src="assets/img/student-choose/student-chourse-4.jpg" alt="image not found"/>
                                        </div>
                                        <div className="course-cart">
                                            <div className="course-info-wrapper">
                                                <div className="cart-info-body">
                                                    <span className="category-color category-color-3"><Link href="/course"><a>Business</a></Link></span>
                                                    <Link href="/course-details"><a><h3>MySQL Database : Beginner SQL Database Design</h3></a></Link>
                                                    <div className="cart-lavel">
                                                        <h5>Level : <span>Beginner</span></h5>
                                                        <p>Knowledge is power. Information is liberating. Education is the premise
                                                            of progress, in every society, in every family</p>
                                                    </div>
                                                    <div className="info-cart-text">
                                                        <ul>
                                                            <li><i className="far fa-check"></i>Scratch to HTML</li>
                                                            <li><i className="far fa-check"></i>Learn how to code in Python</li>
                                                            <li><i className="far fa-check"></i>Unlimited backend database creation</li>
                                                            <li><i className="far fa-check"></i>Adobe XD Tutorials</li>
                                                        </ul>
                                                    </div>
                                                    <div className="course-action">
                                                        <Link href="/course-details"><a className="view-details-btn">View Details</a></Link>
                                                        <button className="wishlist-btn"><i className="flaticon-like"></i></button>
                                                        <Link href="/course-details"><a className="c-share-btn"><i className="flaticon-previous"></i></a></Link>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="student-course-footer">
                                            <div className="student-course-linkter">
                                                <div className="course-lessons">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                                        viewBox="0 0 16.471 16.471">
                                                        <g id="blackboard-3" transform="translate(-0.008)">
                                                            <path id="Path_104" data-name="Path 101"
                                                                d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                                fill="#575757" />
                                                        </g>
                                                    </svg>
                                                    <span className="ms-2">12 Lessons</span>
                                                </div>
                                                <div className="portfolio-price">
                                                    <span>$12.57</span>
                                                    <del>$24.50</del>
                                                </div>
                                            </div>
                                            <div className="student-course-text">
                                                <h3><Link href="/course-details"><a>MySQL Database : Beginner SQL Database Design</a></Link></h3>
                                            </div>
                                            <div className="portfolio-user">
                                                <div className="user-icon">
                                                    <Link href="/instructor-profile"><a><span>Stephen</span></a></Link>
                                                </div>
                                                <div className="course-icon">
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <span>4.9(25)</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-xl-3 col-lg-4 col-md-6  grid-item c-4">
                                    <div className="course-wrapper-2 mb-30">
                                        <div className="student-course-img">
                                            <img src="assets/img/student-choose/student-chourse-5.jpg" alt="image not found"/>
                                        </div>
                                        <div className="course-cart">
                                            <div className="course-info-wrapper">
                                                <div className="cart-info-body">
                                                    <span className="category-color category-color-2"><Link href="/course"><a>Life Style</a></Link></span>
                                                    <Link href="/course-details"><a><h3>Become a Super Human: Naturally and Safely Boost</h3></a></Link>
                                                    <div className="cart-lavel">
                                                        <h5>Level : <span>Beginner</span></h5>
                                                        <p>Knowledge is power. Information is liberating. Education is the premise
                                                            of progress, in every society, in every family</p>
                                                    </div>
                                                    <div className="info-cart-text">
                                                        <ul>
                                                            <li><i className="far fa-check"></i>Scratch to HTML</li>
                                                            <li><i className="far fa-check"></i>Learn how to code in Python</li>
                                                            <li><i className="far fa-check"></i>Unlimited backend database creation</li>
                                                            <li><i className="far fa-check"></i>Adobe XD Tutorials</li>
                                                        </ul>
                                                    </div>
                                                    <div className="course-action">
                                                        <Link href="/course-details"><a className="view-details-btn">View Details</a></Link>
                                                        <button className="wishlist-btn"><i className="flaticon-like"></i></button>
                                                        <Link href="/course-details"><a className="c-share-btn"><i className="flaticon-previous"></i></a></Link>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="student-course-footer">
                                            <div className="student-course-linkter">
                                                <div className="course-lessons">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                                        viewBox="0 0 16.471 16.471">
                                                        <g id="blackboard-4" transform="translate(-0.008)">
                                                            <path id="Path_105" data-name="Path 101"
                                                                d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                                fill="#575757" />
                                                        </g>
                                                    </svg>
                                                    <span className="ms-2">12 Lessons</span>
                                                </div>
                                                <div className="portfolio-price">
                                                    <span>$12.57</span>
                                                    <del>$24.50</del>
                                                </div>
                                            </div>
                                            <div className="student-course-text">
                                                <h3><Link href="/course-details"><a>Become a Super Human: Naturally and Safely Boost</a></Link></h3>
                                            </div>
                                            <div className="portfolio-user">
                                                <div className="user-icon">
                                                    <Link href="/instructor-profile"><a><span>Henry</span></a></Link>
                                                </div>
                                                <div className="course-icon">
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <span>4.9(25)</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-xl-3 col-lg-4 col-md-6  grid-item c-1">
                                    <div className="course-wrapper-2 mb-30">
                                        <div className="student-course-img">
                                            <img src="assets/img/student-choose/student-chourse-6.jpg" alt="image not found"/>
                                        </div>
                                        <div className="course-cart">
                                            <div className="course-info-wrapper">
                                                <div className="cart-info-body">
                                                    <span className="category-color category-color-5"><Link href="/course"><a>Data Science</a></Link></span>
                                                    <Link href="/course-details"><a><h3>Data Science Real-Life Musical Science Exercises Included</h3></a></Link>
                                                    <div className="cart-lavel">
                                                        <h5>Level : <span>Beginner</span></h5>
                                                        <p>Knowledge is power. Information is liberating. Education is the premise
                                                            of progress, in every society, in every family</p>
                                                    </div>
                                                    <div className="info-cart-text">
                                                        <ul>
                                                            <li><i className="far fa-check"></i>Scratch to HTML</li>
                                                            <li><i className="far fa-check"></i>Learn how to code in Python</li>
                                                            <li><i className="far fa-check"></i>Unlimited backend database creation</li>
                                                            <li><i className="far fa-check"></i>Adobe XD Tutorials</li>
                                                        </ul>
                                                    </div>
                                                    <div className="course-action">
                                                        <Link href="/course-details"><a className="view-details-btn">View Details</a></Link>
                                                        <button className="wishlist-btn"><i className="flaticon-like"></i></button>
                                                        <Link href="/course-details"><a className="c-share-btn"><i className="flaticon-previous"></i></a></Link>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="student-course-footer">
                                            <div className="student-course-linkter">
                                                <div className="course-lessons">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                                        viewBox="0 0 16.471 16.471">
                                                        <g id="blackboard-41" transform="translate(-0.008)">
                                                            <path id="Path_1055" data-name="Path 101"
                                                                d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                                fill="#575757" />
                                                        </g>
                                                    </svg>
                                                    <span className="ms-2">12 Lessons</span>
                                                </div>
                                                <div className="portfolio-price">
                                                    <span>$12.57</span>
                                                    <del>$24.50</del>
                                                </div>
                                            </div>
                                            <div className="student-course-text">
                                                <h3><Link href="/course-details"><a>Data Science Real-Life Musical Science Exercises Included</a></Link></h3>
                                            </div>
                                            <div className="portfolio-user">
                                                <div className="user-icon">
                                                    <Link href="/instructor-profile"><a><span>Hozaron</span></a></Link>
                                                </div>
                                                <div className="course-icon">
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <span>4.9(25)</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-xl-3 col-lg-4 col-md-6  grid-item c-1">
                                    <div className="course-wrapper-2 mb-30">
                                        <div className="student-course-img">
                                            <img src="assets/img/student-choose/student-chourse-7.jpg" alt="image not found"/>
                                        </div>
                                        <div className="course-cart">
                                            <div className="course-info-wrapper">
                                                <div className="cart-info-body">
                                                    <span className="category-color category-color-5"><Link href="/course"><a>Data Science</a></Link></span>
                                                    <Link href="/course-details"><a><h3>Data Science Real-Life Data Science Exercises Included</h3></a></Link>
                                                    <div className="cart-lavel">
                                                        <h5>Level : <span>Beginner</span></h5>
                                                        <p>Knowledge is power. Information is liberating. Education is the premise
                                                            of progress, in every society, in every family</p>
                                                    </div>
                                                    <div className="info-cart-text">
                                                        <ul>
                                                            <li><i className="far fa-check"></i>Scratch to HTML</li>
                                                            <li><i className="far fa-check"></i>Learn how to code in Python</li>
                                                            <li><i className="far fa-check"></i>Unlimited backend database creation</li>
                                                            <li><i className="far fa-check"></i>Adobe XD Tutorials</li>
                                                        </ul>
                                                    </div>
                                                    <div className="course-action">
                                                        <Link href="/course-details"><a className="view-details-btn">View Details</a></Link>
                                                        <button className="wishlist-btn"><i className="flaticon-like"></i></button>
                                                        <Link href="/course-details"><a className="c-share-btn"><i className="flaticon-previous"></i></a></Link>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="student-course-footer">
                                            <div className="student-course-linkter">
                                                <div className="course-lessons">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                                        viewBox="0 0 16.471 16.471">
                                                        <g id="blackboard-6" transform="translate(-0.008)">
                                                            <path id="Path_107" data-name="Path 101"
                                                                d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                                fill="#575757" />
                                                        </g>
                                                    </svg>
                                                    <span className="ms-2">12 Lessons</span>
                                                </div>
                                                <div className="portfolio-price">
                                                    <span>FREE</span>
                                                </div>
                                            </div>
                                            <div className="student-course-text">
                                                <h3><Link href="/course-details"><a>Data Science Real-Life Data Science Exercises Included</a></Link></h3>
                                            </div>
                                            <div className="portfolio-user">
                                                <div className="user-icon">
                                                    <Link href="/instructor-profile"><a><span>Sarfaraz</span></a></Link>
                                                </div>
                                                <div className="course-icon">
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <span>4.9(25)</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-xl-3 col-lg-4 col-md-6  grid-item c-3 c-2">
                                    <div className="course-wrapper-2 mb-30">
                                        <div className="student-course-img">
                                            <img src="assets/img/student-choose/student-chourse-8.jpg" alt="image not found"/>
                                        </div>
                                        <div className="course-cart">
                                            <div className="course-info-wrapper">
                                                <div className="cart-info-body">
                                                    <span className="category-color category-color-1"><Link href="/course"><a>Development</a></Link></span>
                                                    <Link href="/course-details"><a><h3>Python and Django Full Stack Web Developer Bootcamp</h3></a></Link>
                                                    <div className="cart-lavel">
                                                        <h5>Level : <span>Beginner</span></h5>
                                                        <p>Knowledge is power. Information is liberating. Education is the premise
                                                            of progress, in every society, in every family</p>
                                                    </div>
                                                    <div className="info-cart-text">
                                                        <ul>
                                                            <li><i className="far fa-check"></i>Scratch to HTML</li>
                                                            <li><i className="far fa-check"></i>Learn how to code in Python</li>
                                                            <li><i className="far fa-check"></i>Unlimited backend database creation</li>
                                                            <li><i className="far fa-check"></i>Adobe XD Tutorials</li>
                                                        </ul>
                                                    </div>
                                                    <div className="course-action">
                                                        <Link href="/course-details"><a className="view-details-btn">View Details</a></Link>
                                                        <button className="wishlist-btn"><i className="flaticon-like"></i></button>
                                                        <Link href="/course-details"><a className="c-share-btn"><i className="flaticon-previous"></i></a></Link>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="student-course-footer">
                                            <div className="student-course-linkter">
                                                <div className="course-lessons">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                                        viewBox="0 0 16.471 16.471">
                                                        <g id="blackboard-7" transform="translate(-0.008)">
                                                            <path id="Path_108" data-name="Path 101"
                                                                d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                                fill="#575757" />
                                                        </g>
                                                    </svg>
                                                    <span className="ms-2">12 Lessons</span>
                                                </div>
                                                <div className="portfolio-price">
                                                    <span>$12.57</span>
                                                    <del>$24.50</del>
                                                </div>
                                            </div>
                                            <div className="student-course-text">
                                                <h3><Link href="/course-details"><a>Python and Django Full Stack Web Developer Bootcamp</a></Link></h3>
                                            </div>
                                            <div className="portfolio-user">
                                                <div className="user-icon">
                                                    <Link href="/instructor-profile"><a><span>Sizan</span></a></Link>
                                                </div>
                                                <div className="course-icon">
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <span>4.9(25)</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <div className="tab-pane fade" id="nav-profile" role="tabpanel" aria-labelledby="nav-profile-tab">
                            <div className='row'>
                                <div className="col-xl-3 col-lg-4 col-md-6  grid-item c-3 c-2">
                                    <div className="course-wrapper-2 mb-30">
                                        <div className="student-course-img">
                                            <img src="assets/img/student-choose/student-chourse-8.jpg" alt="image not found"/>
                                        </div>
                                        <div className="course-cart">
                                            <div className="course-info-wrapper">
                                                <div className="cart-info-body">
                                                    <span className="category-color category-color-1"><Link href="/course"><a>Development</a></Link></span>
                                                    <Link href="/course-details"><a><h3>Python and Django Full Stack Web Developer Bootcamp</h3></a></Link>
                                                    <div className="cart-lavel">
                                                        <h5>Level : <span>Beginner</span></h5>
                                                        <p>Knowledge is power. Information is liberating. Education is the premise
                                                            of progress, in every society, in every family</p>
                                                    </div>
                                                    <div className="info-cart-text">
                                                        <ul>
                                                            <li><i className="far fa-check"></i>Scratch to HTML</li>
                                                            <li><i className="far fa-check"></i>Learn how to code in Python</li>
                                                            <li><i className="far fa-check"></i>Unlimited backend database creation</li>
                                                            <li><i className="far fa-check"></i>Adobe XD Tutorials</li>
                                                        </ul>
                                                    </div>
                                                    <div className="course-action">
                                                        <Link href="/course-details"><a className="view-details-btn">View Details</a></Link>
                                                        <button className="wishlist-btn"><i className="flaticon-like"></i></button>
                                                        <Link href="/course-details"><a className="c-share-btn"><i className="flaticon-previous"></i></a></Link>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="student-course-footer">
                                            <div className="student-course-linkter">
                                                <div className="course-lessons">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                                        viewBox="0 0 16.471 16.471">
                                                        <g id="blackboard-7" transform="translate(-0.008)">
                                                            <path id="Path_108" data-name="Path 101"
                                                                d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                                fill="#575757" />
                                                        </g>
                                                    </svg>
                                                    <span className="ms-2">12 Lessons</span>
                                                </div>
                                                <div className="portfolio-price">
                                                    <span>$12.57</span>
                                                    <del>$24.50</del>
                                                </div>
                                            </div>
                                            <div className="student-course-text">
                                                <h3><Link href="/course-details"><a>Python and Django Full Stack Web Developer Bootcamp</a></Link></h3>
                                            </div>
                                            <div className="portfolio-user">
                                                <div className="user-icon">
                                                    <Link href="/instructor-profile"><a><span>Sizan</span></a></Link>
                                                </div>
                                                <div className="course-icon">
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <span>4.9(25)</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="tab-pane fade" id="nav-contact" role="tabpanel" aria-labelledby="nav-contact-tab">
                            <div className='row'>
                                <div className="col-xl-3 col-lg-4 col-md-6  grid-item c-4">
                                    <div className="course-wrapper-2 mb-30">
                                        <div className="student-course-img">
                                            <img src="assets/img/student-choose/student-chourse-5.jpg" alt="image not found"/>
                                        </div>
                                        <div className="course-cart">
                                            <div className="course-info-wrapper">
                                                <div className="cart-info-body">
                                                    <span className="category-color category-color-2"><Link href="/course"><a>Life Style</a></Link></span>
                                                    <Link href="/course-details"><a><h3>Become a Super Human: Naturally and Safely Boost</h3></a></Link>
                                                    <div className="cart-lavel">
                                                        <h5>Level : <span>Beginner</span></h5>
                                                        <p>Knowledge is power. Information is liberating. Education is the premise
                                                            of progress, in every society, in every family</p>
                                                    </div>
                                                    <div className="info-cart-text">
                                                        <ul>
                                                            <li><i className="far fa-check"></i>Scratch to HTML</li>
                                                            <li><i className="far fa-check"></i>Learn how to code in Python</li>
                                                            <li><i className="far fa-check"></i>Unlimited backend database creation</li>
                                                            <li><i className="far fa-check"></i>Adobe XD Tutorials</li>
                                                        </ul>
                                                    </div>
                                                    <div className="course-action">
                                                        <Link href="/course-details"><a className="view-details-btn">View Details</a></Link>
                                                        <button className="wishlist-btn"><i className="flaticon-like"></i></button>
                                                        <Link href="/course-details"><a className="c-share-btn"><i className="flaticon-previous"></i></a></Link>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="student-course-footer">
                                            <div className="student-course-linkter">
                                                <div className="course-lessons">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                                        viewBox="0 0 16.471 16.471">
                                                        <g id="blackboard-4" transform="translate(-0.008)">
                                                            <path id="Path_105" data-name="Path 101"
                                                                d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                                fill="#575757" />
                                                        </g>
                                                    </svg>
                                                    <span className="ms-2">12 Lessons</span>
                                                </div>
                                                <div className="portfolio-price">
                                                    <span>$12.57</span>
                                                    <del>$24.50</del>
                                                </div>
                                            </div>
                                            <div className="student-course-text">
                                                <h3><Link href="/course-details"><a>Become a Super Human: Naturally and Safely Boost</a></Link></h3>
                                            </div>
                                            <div className="portfolio-user">
                                                <div className="user-icon">
                                                    <Link href="/instructor-profile"><a><span>Henry</span></a></Link>
                                                </div>
                                                <div className="course-icon">
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <span>4.9(25)</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-xl-3 col-lg-4 col-md-6  grid-item c-1">
                                    <div className="course-wrapper-2 mb-30">
                                        <div className="student-course-img">
                                            <img src="assets/img/student-choose/student-chourse-6.jpg" alt="image not found"/>
                                        </div>
                                        <div className="course-cart">
                                            <div className="course-info-wrapper">
                                                <div className="cart-info-body">
                                                    <span className="category-color category-color-5"><Link href="/course"><a>Data Science</a></Link></span>
                                                    <Link href="/course-details"><a><h3>Data Science Real-Life Musical Science Exercises Included</h3></a></Link>
                                                    <div className="cart-lavel">
                                                        <h5>Level : <span>Beginner</span></h5>
                                                        <p>Knowledge is power. Information is liberating. Education is the premise
                                                            of progress, in every society, in every family</p>
                                                    </div>
                                                    <div className="info-cart-text">
                                                        <ul>
                                                            <li><i className="far fa-check"></i>Scratch to HTML</li>
                                                            <li><i className="far fa-check"></i>Learn how to code in Python</li>
                                                            <li><i className="far fa-check"></i>Unlimited backend database creation</li>
                                                            <li><i className="far fa-check"></i>Adobe XD Tutorials</li>
                                                        </ul>
                                                    </div>
                                                    <div className="course-action">
                                                        <Link href="/course-details"><a className="view-details-btn">View Details</a></Link>
                                                        <button className="wishlist-btn"><i className="flaticon-like"></i></button>
                                                        <Link href="/course-details"><a className="c-share-btn"><i className="flaticon-previous"></i></a></Link>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="student-course-footer">
                                            <div className="student-course-linkter">
                                                <div className="course-lessons">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                                        viewBox="0 0 16.471 16.471">
                                                        <g id="blackboard-41" transform="translate(-0.008)">
                                                            <path id="Path_1055" data-name="Path 101"
                                                                d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                                fill="#575757" />
                                                        </g>
                                                    </svg>
                                                    <span className="ms-2">12 Lessons</span>
                                                </div>
                                                <div className="portfolio-price">
                                                    <span>$12.57</span>
                                                    <del>$24.50</del>
                                                </div>
                                            </div>
                                            <div className="student-course-text">
                                                <h3><Link href="/course-details"><a>Data Science Real-Life Musical Science Exercises Included</a></Link></h3>
                                            </div>
                                            <div className="portfolio-user">
                                                <div className="user-icon">
                                                    <Link href="/instructor-profile"><a><span>Hozaron</span></a></Link>
                                                </div>
                                                <div className="course-icon">
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <span>4.9(25)</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-xl-3 col-lg-4 col-md-6  grid-item c-1">
                                    <div className="course-wrapper-2 mb-30">
                                        <div className="student-course-img">
                                            <img src="assets/img/student-choose/student-chourse-7.jpg" alt="image not found"/>
                                        </div>
                                        <div className="course-cart">
                                            <div className="course-info-wrapper">
                                                <div className="cart-info-body">
                                                    <span className="category-color category-color-5"><Link href="/course"><a>Data Science</a></Link></span>
                                                    <Link href="/course-details"><a><h3>Data Science Real-Life Data Science Exercises Included</h3></a></Link>
                                                    <div className="cart-lavel">
                                                        <h5>Level : <span>Beginner</span></h5>
                                                        <p>Knowledge is power. Information is liberating. Education is the premise
                                                            of progress, in every society, in every family</p>
                                                    </div>
                                                    <div className="info-cart-text">
                                                        <ul>
                                                            <li><i className="far fa-check"></i>Scratch to HTML</li>
                                                            <li><i className="far fa-check"></i>Learn how to code in Python</li>
                                                            <li><i className="far fa-check"></i>Unlimited backend database creation</li>
                                                            <li><i className="far fa-check"></i>Adobe XD Tutorials</li>
                                                        </ul>
                                                    </div>
                                                    <div className="course-action">
                                                        <Link href="/course-details"><a className="view-details-btn">View Details</a></Link>
                                                        <button className="wishlist-btn"><i className="flaticon-like"></i></button>
                                                        <Link href="/course-details"><a className="c-share-btn"><i className="flaticon-previous"></i></a></Link>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="student-course-footer">
                                            <div className="student-course-linkter">
                                                <div className="course-lessons">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                                        viewBox="0 0 16.471 16.471">
                                                        <g id="blackboard-6" transform="translate(-0.008)">
                                                            <path id="Path_107" data-name="Path 101"
                                                                d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                                fill="#575757" />
                                                        </g>
                                                    </svg>
                                                    <span className="ms-2">12 Lessons</span>
                                                </div>
                                                <div className="portfolio-price">
                                                    <span>FREE</span>
                                                </div>
                                            </div>
                                            <div className="student-course-text">
                                                <h3><Link href="/course-details"><a>Data Science Real-Life Data Science Exercises Included</a></Link></h3>
                                            </div>
                                            <div className="portfolio-user">
                                                <div className="user-icon">
                                                    <Link href="/instructor-profile"><a><span>Sarfaraz</span></a></Link>
                                                </div>
                                                <div className="course-icon">
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <span>4.9(25)</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="tab-pane fade" id="nav-contactA" role="tabpanel" aria-labelledby="nav-contact-tabA">
                            <div className='row'>
                                <div className="col-xl-3 col-lg-4 col-md-6  grid-item c-3">
                                    <div className="course-wrapper-2 mb-30">
                                        <div className="student-course-img">
                                            <img src="assets/img/student-choose/student-chourse-2.jpg" alt="image not found"/>
                                        </div>
                                        <div className="course-cart">
                                            <div className="course-info-wrapper">
                                                <div className="cart-info-body">
                                                    <span className="category-color category-color-3"><Link href="/course"><a>Business</a></Link></span>
                                                    <Link href="/course-details"><a><h3>Write Better Emails: Tactics for Smarter Team Communication</h3></a></Link>
                                                    <div className="cart-lavel">
                                                        <p>Level : <span>Beginner</span></p>
                                                        <p>Knowledge is power. Information is liberating. Education is the premise
                                                            of progress, in every society, in every family</p>
                                                    </div>
                                                    <div className="info-cart-text">
                                                        <ul>
                                                            <li><i className="far fa-check"></i>Scratch to HTML</li>
                                                            <li><i className="far fa-check"></i>Learn how to code in Python</li>
                                                            <li><i className="far fa-check"></i>Unlimited backend database creation</li>
                                                            <li><i className="far fa-check"></i>Adobe XD Tutorials</li>
                                                        </ul>
                                                    </div>
                                                    <div className="course-action">
                                                        <Link href="/course-details"><a className="view-details-btn">View Details</a></Link>
                                                        <button className="wishlist-btn"><i className="flaticon-like"></i></button>
                                                        <Link href="/course-details"><a className="c-share-btn"><i className="flaticon-previous"></i></a></Link>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="student-course-footer">
                                            <div className="student-course-linkter">
                                                <div className="course-lessons">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                                        viewBox="0 0 16.471 16.471">
                                                        <g id="blackboard-1" transform="translate(-0.008)">
                                                            <path id="Path_102" data-name="Path 101"
                                                                d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                                fill="#575757" />
                                                        </g>
                                                    </svg>
                                                    <span className="ms-2">12 Lessons</span>
                                                </div>
                                                <div className="portfolio-price">
                                                    <span>$12.57</span>
                                                    <del>$24.50</del>
                                                </div>
                                            </div>
                                            <div className="student-course-text">
                                                <h3><Link href="/course-details"><a>Write Better Emails: Tactics for Smarter Team Communication</a></Link></h3>
                                            </div>
                                            <div className="portfolio-user">
                                                <div className="user-icon">
                                                    <Link href="/instructor-profile"><a><span>Dcosta</span></a></Link>
                                                </div>
                                                <div className="course-icon">
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <span>4.9(25)</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="tab-pane fade" id="nav-contactB" role="tabpanel" aria-labelledby="nav-contact-tabB">
                            <div className='row'>
                                <div className="col-xl-3 col-lg-4 col-md-6  grid-item c-2">
                                    <div className="course-wrapper-2 mb-30">
                                        <div className="student-course-img">
                                            <img src="assets/img/student-choose/student-chourse-1.jpg" alt="image not found"/>
                                        </div>
                                        <div className="course-cart">
                                            <div className="course-info-wrapper">
                                                <div className="cart-info-body">
                                                    <span className="category-color category-color-1"><Link href="/course"><a>Development</a></Link></span>
                                                    <Link href="/course-details"><a><h3>WordPress Development Course for Plugins and Themes</h3></a></Link>
                                                    <div className="cart-lavel">
                                                        <h5>Level : <span>Beginner</span></h5>
                                                        <p>Knowledge is power. Information is liberating. Education is the premise
                                                            of progress, in every society, in every family</p>
                                                    </div>
                                                    <div className="info-cart-text">
                                                        <ul>
                                                            <li><i className="far fa-check"></i>Scratch to HTML</li>
                                                            <li><i className="far fa-check"></i>Learn how to code in Python</li>
                                                            <li><i className="far fa-check"></i>Unlimited backend database creation</li>
                                                            <li><i className="far fa-check"></i>Adobe XD Tutorials</li>
                                                        </ul>
                                                    </div>
                                                    <div className="course-action">
                                                        <Link href="/course-details"><a className="view-details-btn">View Details</a></Link>
                                                        <button className="wishlist-btn"><i className="flaticon-like"></i></button>
                                                        <Link href="/course-details"><a className="c-share-btn"><i className="flaticon-previous"></i></a></Link>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="student-course-footer">
                                            <div className="student-course-linkter">
                                                <div className="course-lessons">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                                        viewBox="0 0 16.471 16.471">
                                                        <g id="blackboard-8" transform="translate(-0.008)">
                                                            <path id="Path_106" data-name="Path 101"
                                                                d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                                fill="#575757" />
                                                        </g>
                                                    </svg>
                                                    <span className="ms-2">12 Lessons</span>
                                                </div>
                                                <div className="portfolio-price">
                                                    <span>$12.57</span>
                                                    <del>$24.50</del>
                                                </div>
                                            </div>
                                            <div className="student-course-text">
                                                <h3><Link href="/course-details"><a>WordPress Development Course for Plugins Themes</a></Link></h3>
                                            </div>
                                            <div className="portfolio-user">
                                                <div className="user-icon">
                                                    <Link href="/instructor-profile"><a><span>Danial</span></a></Link>
                                                </div>
                                                <div className="course-icon">
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <i className="fas fa-star"></i>
                                                    <span>4.9(25)</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="row justify-content-center">
                    <div className="col-xl-3">
                        <div className="portfolio-brn mt-20 text-center">
                            <Link href="/course"><a className="edu-sec-btn">View all courses</a></Link>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default CourseTabTwo;