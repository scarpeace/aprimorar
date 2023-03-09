import React from 'react';
import Link from 'next/link';

const CourseTab = () => {
    return (
        <section className="course-area p-relative pt-110 pb-90">
         <div className="course-shape-1">
            <img src="assets/img/shape/portfolio-shap-1.png" alt="shape"/>
         </div>
         <div className="course-shape-2">
            <img src="assets/img/shape/portfolio-shap-2.png" alt="shape"/>
         </div>
         <div className="course-shape-3">
            <img src="assets/img/shape/portfolio-shap-3.png" alt="shape"/>
         </div>
         <div className="container">
            <div className="row">
               <div className="col-xl-5 col-lg-5 f-left">
                  <div className="section-title mb-50">
                     <h2>Discover<br/>
                        World's Best <span className="down-mark-line">Courses</span></h2>
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
                            <div className="col-xl-4 col-lg-4 col-md-6">
                                <div className="eduman-course-main-wrapper mb-30">
                                    <div className="course-cart">
                                        <div className="course-info-wrapper">
                                        <div className="cart-info-body">
                                            <span className="category-color category-color-1"><Link href="/course"><a>Development</a></Link></span>
                                            <Link href="/course-details"><a><h3>WordPress Development Course for Plugins and Themes</h3></a></Link>
                                            <div className="cart-lavel">
                                                <h5>Level : <span>Beginner</span></h5>
                                                <p>Knowledge is power. Information is liberating. Education is the premise of
                                                    progress, in every society, in every family</p>
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
                                    <div className="eduman-course-thumb w-img">
                                        <Link href="/course-details"><a><img src="assets/img/portfilo/course-img-01.jpg" alt="course-img"/></a></Link>
                                    </div>
                                    <div className="eduman-course-wraper">
                                        <div className="eduman-course-heading">
                                            <Link href="/course"><a className="course-link-color-1">Development</a></Link>
                                            <span className="couse-star"><i className="fas fa-star"></i>4.9 (25)</span>
                                        </div>
                                        <div className="eduman-course-text">
                                            <h3><Link href="/course-details"><a>WordPress Development Course for Plugins and Themes</a></Link></h3>
                                        </div>
                                        <div className="eduman-course-meta">
                                        <div className="eduman-course-price">
                                            <span className="price-now">$47.00 </span>
                                            <del className="price-old">$75.50</del>
                                        </div>
                                        <div className="eduman-course-tutor"><img src="assets/img/portfilo/course-tutor-01.png"
                                                alt="image not found"/>
                                            <Link href="/instructor-profile"><a><span>Danial</span></a></Link>
                                        </div>
                                        </div>
                                    </div>
                                    <div className="eduman-course-footer">
                                        <div className="course-lessson-svg">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                            viewBox="0 0 16.471 16.471">
                                            <g id="blackboar09" transform="translate(-0.008)">
                                                <path id="Path_01" data-name="Path 101"
                                                    d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                    fill="#575757" />
                                            </g>
                                        </svg>
                                        <span className="ms-2">12 Lessons</span>
                                        </div>
                                        <div className="course-deteals-btn">
                                            <Link href="/course-details"><a><span className="me-2">View Details</span><i className="far fa-arrow-right"></i></a></Link>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="col-xl-4 col-lg-4 col-md-6">
                                <div className="eduman-course-main-wrapper mb-30">
                                    <div className="course-cart">
                                        <div className="course-info-wrapper">
                                        <div className="cart-info-body">
                                            <span className="category-color category-color-1"><Link href="/course"><a>Development</a></Link></span>
                                            <Link href="/course-details"><a><h3>Master Google Docs: Free online documents for personal use</h3></a></Link>
                                            <div className="cart-lavel">
                                                <h5>Level : <span>Beginner</span></h5>
                                                <p>Knowledge is power. Information is liberating. Education is the premise of
                                                    progress, in every society, in every family</p>
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
                                    <div className="eduman-course-thumb w-img">
                                        <Link href="/course-details"><a><img src="assets/img/portfilo/course-img-02.jpg" alt="course-img"/></a></Link>
                                    </div>
                                    <div className="eduman-course-wraper">
                                        <div className="eduman-course-heading">
                                        <Link href="/course"><a className="course-link-color-1">Development</a></Link>
                                        <span className="couse-star"><i className="fas fa-star"></i>4.9 (25)</span>
                                        </div>
                                        <div className="eduman-course-text">
                                            <h3><Link href="/course-details"><a>Master Google Docs: Free online documents for personal use</a></Link></h3>
                                        </div>
                                        <div className="eduman-course-meta">
                                        <div className="eduman-course-price">
                                            <span className="price-now">$47.00 </span>
                                        </div>
                                        <div className="eduman-course-tutor">
                                            <img src="assets/img/portfilo/course-tutor-02.png" alt="image not found"/>
                                            <Link href="/instructor-profile"><a><span>Mark Hanry</span></a></Link>
                                        </div>
                                        </div>
                                    </div>
                                    <div className="eduman-course-footer">
                                        <div className="course-lessson-svg">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                            viewBox="0 0 16.471 16.471">
                                            <g id="blackboar09154" transform="translate(-0.008)">
                                                <path id="Path_2501" data-name="Path 101"
                                                    d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                    fill="#575757" />
                                            </g>
                                        </svg>
                                        <span className="ms-2">12 Lessons</span>
                                        </div>
                                        <div className="course-deteals-btn">
                                            <Link href="/course-details"><a><span className="me-2">View Details</span><i className="far fa-arrow-right"></i></a></Link>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="col-xl-4 col-lg-4 col-md-6">
                                <div className="eduman-course-main-wrapper mb-30">
                                    <div className="course-cart">
                                        <div className="course-info-wrapper">
                                        <div className="cart-info-body">
                                            <span className="category-color category-color-3"><Link href="/course"><a>Business</a></Link></span>
                                            <Link href="/course-details"><a><h3>Write Better Emails: Tactics for Smarter Team Communication</h3></a></Link>
                                            <div className="cart-lavel">
                                                <h5>Level : <span>Beginner</span></h5>
                                                <p>Knowledge is power. Information is liberating. Education is the premise of
                                                    progress, in every society, in every family</p>
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
                                    <div className="eduman-course-thumb w-img">
                                        <Link href="/course-details"><a><img src="assets/img/portfilo/course-img-03.jpg" alt="course-img"/></a></Link>
                                    </div>
                                    <div className="eduman-course-wraper">
                                        <div className="eduman-course-heading">
                                        <Link href="/course"><a className="course-link-color-3">Business</a></Link>
                                        <span className="couse-star"><i className="fas fa-star"></i>4.9 (25)</span>
                                        </div>
                                        <div className="eduman-course-text">
                                            <h3><Link href="/course-details"><a>Write Better Emails: Tactics for Smarter Team Communication</a></Link></h3>
                                        </div>
                                        <div className="eduman-course-meta">
                                        <div className="eduman-course-price">
                                            <span className="price-now">FREE </span>
                                        </div>
                                        <div className="eduman-course-tutor">
                                            <img src="assets/img/portfilo/course-tutor-03.png" alt="image not found"/>
                                            <Link href="/instructor-profile"><a><span>Junior Lucy</span></a></Link>
                                        </div>
                                        </div>
                                    </div>
                                    <div className="eduman-course-footer">
                                        <div className="course-lessson-svg">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                            viewBox="0 0 16.471 16.471">
                                            <g id="blackboard-11" transform="translate(-0.008)">
                                                <path id="Path_00001" data-name="Path 101"
                                                    d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                    fill="#575757" />
                                            </g>
                                        </svg>
                                        <span className="ms-2">12 Lessons</span>
                                        </div>
                                        <div className="course-deteals-btn">
                                        <Link href="/course-details"><a><span className="me-2">View Details</span><i className="far fa-arrow-right"></i></a></Link>
                                    </div>
                                </div>
                                </div>
                            </div>
                            <div className="col-xl-4 col-lg-4 col-md-6">
                                <div className="eduman-course-main-wrapper mb-30">
                                    <div className="course-cart">
                                        <div className="course-info-wrapper">
                                        <div className="cart-info-body">
                                            <span className="category-color category-color-1"><Link href="/course"><a>Development</a></Link></span>
                                            <Link href="/course-details"><a><h3>Python and Django Full Stack Web Developer Bootcamp</h3></a></Link>
                                            <div className="cart-lavel">
                                                <h5>Level : <span>Beginner</span></h5>
                                                <p>Knowledge is power. Information is liberating. Education is the premise of
                                                    progress, in every society, in every family</p>
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
                                    <div className="eduman-course-thumb w-img">
                                        <Link href="/course-details"><a><img src="assets/img/portfilo/course-img-05.jpg" alt="course-img"/></a></Link>
                                    </div>
                                    <div className="eduman-course-wraper">
                                        <div className="eduman-course-heading">
                                        <Link href="/course"><a className="course-link-color-1">Development</a></Link>
                                        <span className="couse-star"><i className="fas fa-star"></i>4.9 (25)</span>
                                        </div>
                                        <div className="eduman-course-text">
                                            <h3><Link href="/course-details"><a>Python and Django Full Stack Web Developer Bootcamp</a></Link></h3>
                                        </div>
                                        <div className="eduman-course-meta">
                                        <div className="eduman-course-price">
                                            <span className="price-now">$47.00 </span>
                                            <del className="price-old">$75.50</del>
                                        </div>
                                        <div className="eduman-course-tutor">
                                            <img src="assets/img/portfilo/course-tutor-05.png" alt="image not found"/>
                                            <Link href="/instructor-profile"><a><span>Edyal Romel</span></a></Link>
                                        </div>
                                        </div>
                                    </div>
                                    <div className="eduman-course-footer">
                                        <div className="course-lessson-svg">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                            viewBox="0 0 16.471 16.471">
                                            <g id="blackboard" transform="translate(-0.008)">
                                                <path id="Path_101" data-name="Path 101"
                                                    d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                    fill="#575757" />
                                            </g>
                                        </svg>
                                        <span className="ms-2">12 Lessons</span>
                                        </div>
                                        <div className="course-deteals-btn">
                                        <Link href="/course-details"><a><span className="me-2">View Details</span><i className="far fa-arrow-right"></i></a></Link>
                                    </div>
                                </div>
                                </div>
                            </div>
                            <div className="col-xl-4 col-lg-4 col-md-6">
                                <div className="eduman-course-main-wrapper mb-30">
                                    <div className="course-cart">
                                        <div className="course-info-wrapper">
                                        <div className="cart-info-body">
                                            <span className="category-color category-color-5"><Link href="/course"><a>Data Science</a></Link></span>
                                            <Link href="/course-details"><a><h3>Data Science Real-Life Data Science Exercises Included</h3></a></Link>
                                            <div className="cart-lavel">
                                                <h5>Level : <span>Beginner</span></h5>
                                                <p>Knowledge is power. Information is liberating. Education is the premise of
                                                    progress, in every society, in every family</p>
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
                                    <div className="eduman-course-thumb w-img">
                                        <Link href="/course-details"><a><img src="assets/img/portfilo/course-img-06.jpg" alt="course-img"/></a></Link>
                                    </div>
                                    <div className="eduman-course-wraper">
                                        <div className="eduman-course-heading">
                                        <Link href="/course"><a className="course-link-color-5">Data Science</a></Link>
                                        <span className="couse-star"><i className="fas fa-star"></i>4.9 (25)</span>
                                        </div>
                                        <div className="eduman-course-text">
                                        <h3><Link href="/course-details"><a>Data Science Real-Life Data Science Exercises Included</a></Link></h3>
                                        </div>
                                        <div className="eduman-course-meta">
                                        <div className="eduman-course-price">
                                            <span className="price-now">$47.00 </span>
                                        </div>
                                        <div className="eduman-course-tutor">
                                            <img src="assets/img/portfilo/course-tutor-06.png" alt="image not found"/>
                                            <Link href="/instructor-profile"><a><span>Mark Hanry</span></a></Link>
                                        </div>
                                        </div>
                                    </div>
                                    <div className="eduman-course-footer">
                                        <div className="course-lessson-svg">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                            viewBox="0 0 16.471 16.471">
                                            <g id="blackboard-13" transform="translate(-0.008)">
                                                <path id="Path_000" data-name="Path 101"
                                                    d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                    fill="#575757" />
                                            </g>
                                        </svg>
                                        <span className="ms-2">12 Lessons</span>
                                        </div>
                                        <div className="course-deteals-btn">
                                            <Link href="/course-details"><a><span className="me-2">View Details</span><i className="far fa-arrow-right"></i></a></Link>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="col-xl-4 col-lg-4 col-md-6">
                                <div className="eduman-course-main-wrapper mb-30">
                                    <div className="course-cart">
                                        <div className="course-info-wrapper">
                                        <div className="cart-info-body">
                                            <span className="category-color category-color-4"><Link href="/course"><a>Life Style</a></Link></span>
                                            <Link href="/course-details"><a><h3>Become a Super Human: Naturally and Safely Boost</h3></a></Link>
                                            <div className="cart-lavel">
                                                <h5>Level : <span>Beginner</span></h5>
                                                <p>Knowledge is power. Information is liberating. Education is the premise of
                                                    progress, in every society, in every family</p>
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
                                    <div className="eduman-course-thumb w-img">
                                        <Link href="/course-details"><a><img src="assets/img/portfilo/course-img-04.jpg" alt="course-img"/></a></Link>
                                    </div>
                                    <div className="eduman-course-wraper">
                                        <div className="eduman-course-heading">
                                        <Link href="/course"><a className="course-link-color-4">Life Style</a></Link>
                                        <span className="couse-star"><i className="fas fa-star"></i>4.9 (25)</span>
                                        </div>
                                        <div className="eduman-course-text">
                                            <h3><Link href="/course-details"><a>Become a Super Human: Naturally and Safely Boost</a></Link></h3>
                                        </div>
                                        <div className="eduman-course-meta">
                                        <div className="eduman-course-price">
                                            <span className="price-now">$47.00</span>
                                        </div>
                                        <div className="eduman-course-tutor">
                                            <img src="assets/img/portfilo/course-tutor-04.png" alt="image not found"/>
                                            <Link href="/instructor-profile"><a><span>Danial</span></a></Link>
                                        </div>
                                        </div>
                                    </div>
                                    <div className="eduman-course-footer">
                                        <div className="course-lessson-svg">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                            viewBox="0 0 16.471 16.471">
                                            <g id="blackboard-10" transform="translate(-0.008)">
                                                <path id="Path_100000" data-name="Path 101"
                                                    d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                    fill="#575757" />
                                            </g>
                                        </svg>
                                        <span className="ms-2">12 Lessons</span>
                                        </div>
                                        <div className="course-deteals-btn">
                                            <Link href="/course-details"><a><span className="me-2">View Details</span><i className="far fa-arrow-right"></i></a></Link>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div className="tab-pane fade" id="nav-profile" role="tabpanel" aria-labelledby="nav-profile-tab">
                        <div className='row'>
                            <div className="col-xl-4 col-lg-4 col-md-6">
                                <div className="eduman-course-main-wrapper mb-30">
                                    <div className="course-cart">
                                        <div className="course-info-wrapper">
                                        <div className="cart-info-body">
                                            <span className="category-color category-color-4"><Link href="/course"><a>Life Style</a></Link></span>
                                            <Link href="/course-details"><a><h3>Become a Super Human: Naturally and Safely Boost</h3></a></Link>
                                            <div className="cart-lavel">
                                                <h5>Level : <span>Beginner</span></h5>
                                                <p>Knowledge is power. Information is liberating. Education is the premise of
                                                    progress, in every society, in every family</p>
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
                                    <div className="eduman-course-thumb w-img">
                                        <Link href="/course-details"><a><img src="assets/img/portfilo/course-img-04.jpg" alt="course-img"/></a></Link>
                                    </div>
                                    <div className="eduman-course-wraper">
                                        <div className="eduman-course-heading">
                                        <Link href="/course"><a className="course-link-color-4">Life Style</a></Link>
                                        <span className="couse-star"><i className="fas fa-star"></i>4.9 (25)</span>
                                        </div>
                                        <div className="eduman-course-text">
                                            <h3><Link href="/course-details"><a>Become a Super Human: Naturally and Safely Boost</a></Link></h3>
                                        </div>
                                        <div className="eduman-course-meta">
                                        <div className="eduman-course-price">
                                            <span className="price-now">$47.00</span>
                                        </div>
                                        <div className="eduman-course-tutor">
                                            <img src="assets/img/portfilo/course-tutor-04.png" alt="image not found"/>
                                            <Link href="/instructor-profile"><a><span>Danial</span></a></Link>
                                        </div>
                                        </div>
                                    </div>
                                    <div className="eduman-course-footer">
                                        <div className="course-lessson-svg">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                            viewBox="0 0 16.471 16.471">
                                            <g id="blackboard-10" transform="translate(-0.008)">
                                                <path id="Path_100000" data-name="Path 101"
                                                    d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                    fill="#575757" />
                                            </g>
                                        </svg>
                                        <span className="ms-2">12 Lessons</span>
                                        </div>
                                        <div className="course-deteals-btn">
                                            <Link href="/course-details"><a><span className="me-2">View Details</span><i className="far fa-arrow-right"></i></a></Link>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="tab-pane fade" id="nav-contact" role="tabpanel" aria-labelledby="nav-contact-tab">
                        <div className='row'>
                            <div className="col-xl-4 col-lg-4 col-md-6">
                                <div className="eduman-course-main-wrapper mb-30">
                                    <div className="course-cart">
                                        <div className="course-info-wrapper">
                                        <div className="cart-info-body">
                                            <span className="category-color category-color-3"><Link href="/course"><a>Business</a></Link></span>
                                            <Link href="/course-details"><a><h3>Write Better Emails: Tactics for Smarter Team Communication</h3></a></Link>
                                            <div className="cart-lavel">
                                                <h5>Level : <span>Beginner</span></h5>
                                                <p>Knowledge is power. Information is liberating. Education is the premise of
                                                    progress, in every society, in every family</p>
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
                                    <div className="eduman-course-thumb w-img">
                                        <Link href="/course-details"><a><img src="assets/img/portfilo/course-img-03.jpg" alt="course-img"/></a></Link>
                                    </div>
                                    <div className="eduman-course-wraper">
                                        <div className="eduman-course-heading">
                                        <Link href="/course"><a className="course-link-color-3">Business</a></Link>
                                        <span className="couse-star"><i className="fas fa-star"></i>4.9 (25)</span>
                                        </div>
                                        <div className="eduman-course-text">
                                            <h3><Link href="/course-details"><a>Write Better Emails: Tactics for Smarter Team Communication</a></Link></h3>
                                        </div>
                                        <div className="eduman-course-meta">
                                        <div className="eduman-course-price">
                                            <span className="price-now">FREE </span>
                                        </div>
                                        <div className="eduman-course-tutor">
                                            <img src="assets/img/portfilo/course-tutor-03.png" alt="image not found"/>
                                            <Link href="/instructor-profile"><a><span>Junior Lucy</span></a></Link>
                                        </div>
                                        </div>
                                    </div>
                                    <div className="eduman-course-footer">
                                        <div className="course-lessson-svg">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                            viewBox="0 0 16.471 16.471">
                                            <g id="blackboard-11" transform="translate(-0.008)">
                                                <path id="Path_00001" data-name="Path 101"
                                                    d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                    fill="#575757" />
                                            </g>
                                        </svg>
                                        <span className="ms-2">12 Lessons</span>
                                        </div>
                                        <div className="course-deteals-btn">
                                        <Link href="/course-details"><a><span className="me-2">View Details</span><i className="far fa-arrow-right"></i></a></Link>
                                    </div>
                                </div>
                                </div>
                            </div>
                            <div className="col-xl-4 col-lg-4 col-md-6">
                                <div className="eduman-course-main-wrapper mb-30">
                                    <div className="course-cart">
                                        <div className="course-info-wrapper">
                                        <div className="cart-info-body">
                                            <span className="category-color category-color-1"><Link href="/course"><a>Development</a></Link></span>
                                            <Link href="/course-details"><a><h3>Python and Django Full Stack Web Developer Bootcamp</h3></a></Link>
                                            <div className="cart-lavel">
                                                <h5>Level : <span>Beginner</span></h5>
                                                <p>Knowledge is power. Information is liberating. Education is the premise of
                                                    progress, in every society, in every family</p>
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
                                    <div className="eduman-course-thumb w-img">
                                        <Link href="/course-details"><a><img src="assets/img/portfilo/course-img-05.jpg" alt="course-img"/></a></Link>
                                    </div>
                                    <div className="eduman-course-wraper">
                                        <div className="eduman-course-heading">
                                        <Link href="/course"><a className="course-link-color-1">Development</a></Link>
                                        <span className="couse-star"><i className="fas fa-star"></i>4.9 (25)</span>
                                        </div>
                                        <div className="eduman-course-text">
                                            <h3><Link href="/course-details"><a>Python and Django Full Stack Web Developer Bootcamp</a></Link></h3>
                                        </div>
                                        <div className="eduman-course-meta">
                                        <div className="eduman-course-price">
                                            <span className="price-now">$47.00 </span>
                                            <del className="price-old">$75.50</del>
                                        </div>
                                        <div className="eduman-course-tutor">
                                            <img src="assets/img/portfilo/course-tutor-05.png" alt="image not found"/>
                                            <Link href="/instructor-profile"><a><span>Edyal Romel</span></a></Link>
                                        </div>
                                        </div>
                                    </div>
                                    <div className="eduman-course-footer">
                                        <div className="course-lessson-svg">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                            viewBox="0 0 16.471 16.471">
                                            <g id="blackboard" transform="translate(-0.008)">
                                                <path id="Path_101" data-name="Path 101"
                                                    d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                    fill="#575757" />
                                            </g>
                                        </svg>
                                        <span className="ms-2">12 Lessons</span>
                                        </div>
                                        <div className="course-deteals-btn">
                                        <Link href="/course-details"><a><span className="me-2">View Details</span><i className="far fa-arrow-right"></i></a></Link>
                                    </div>
                                </div>
                                </div>
                            </div>
                            <div className="col-xl-4 col-lg-4 col-md-6">
                                <div className="eduman-course-main-wrapper mb-30">
                                    <div className="course-cart">
                                        <div className="course-info-wrapper">
                                        <div className="cart-info-body">
                                            <span className="category-color category-color-5"><Link href="/course"><a>Data Science</a></Link></span>
                                            <Link href="/course-details"><a><h3>Data Science Real-Life Data Science Exercises Included</h3></a></Link>
                                            <div className="cart-lavel">
                                                <h5>Level : <span>Beginner</span></h5>
                                                <p>Knowledge is power. Information is liberating. Education is the premise of
                                                    progress, in every society, in every family</p>
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
                                    <div className="eduman-course-thumb w-img">
                                        <Link href="/course-details"><a><img src="assets/img/portfilo/course-img-06.jpg" alt="course-img"/></a></Link>
                                    </div>
                                    <div className="eduman-course-wraper">
                                        <div className="eduman-course-heading">
                                        <Link href="/course"><a className="course-link-color-5">Data Science</a></Link>
                                        <span className="couse-star"><i className="fas fa-star"></i>4.9 (25)</span>
                                        </div>
                                        <div className="eduman-course-text">
                                        <h3><Link href="/course-details"><a>Data Science Real-Life Data Science Exercises Included</a></Link></h3>
                                        </div>
                                        <div className="eduman-course-meta">
                                        <div className="eduman-course-price">
                                            <span className="price-now">$47.00 </span>
                                        </div>
                                        <div className="eduman-course-tutor">
                                            <img src="assets/img/portfilo/course-tutor-06.png" alt="image not found"/>
                                            <Link href="/instructor-profile"><a><span>Mark Hanry</span></a></Link>
                                        </div>
                                        </div>
                                    </div>
                                    <div className="eduman-course-footer">
                                        <div className="course-lessson-svg">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                            viewBox="0 0 16.471 16.471">
                                            <g id="blackboard-13" transform="translate(-0.008)">
                                                <path id="Path_000" data-name="Path 101"
                                                    d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                    fill="#575757" />
                                            </g>
                                        </svg>
                                        <span className="ms-2">12 Lessons</span>
                                        </div>
                                        <div className="course-deteals-btn">
                                            <Link href="/course-details"><a><span className="me-2">View Details</span><i className="far fa-arrow-right"></i></a></Link>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="tab-pane fade" id="nav-contactA" role="tabpanel" aria-labelledby="nav-contact-tabA">
                        <div className='row'>
                            <div className="col-xl-4 col-lg-4 col-md-6">
                                <div className="eduman-course-main-wrapper mb-30">
                                    <div className="course-cart">
                                        <div className="course-info-wrapper">
                                        <div className="cart-info-body">
                                            <span className="category-color category-color-1"><Link href="/course"><a>Development</a></Link></span>
                                            <Link href="/course-details"><a><h3>Master Google Docs: Free online documents for personal use</h3></a></Link>
                                            <div className="cart-lavel">
                                                <h5>Level : <span>Beginner</span></h5>
                                                <p>Knowledge is power. Information is liberating. Education is the premise of
                                                    progress, in every society, in every family</p>
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
                                    <div className="eduman-course-thumb w-img">
                                        <Link href="/course-details"><a><img src="assets/img/portfilo/course-img-02.jpg" alt="course-img"/></a></Link>
                                    </div>
                                    <div className="eduman-course-wraper">
                                        <div className="eduman-course-heading">
                                        <Link href="/course"><a className="course-link-color-1">Development</a></Link>
                                        <span className="couse-star"><i className="fas fa-star"></i>4.9 (25)</span>
                                        </div>
                                        <div className="eduman-course-text">
                                            <h3><Link href="/course-details"><a>Master Google Docs: Free online documents for personal use</a></Link></h3>
                                        </div>
                                        <div className="eduman-course-meta">
                                        <div className="eduman-course-price">
                                            <span className="price-now">$47.00 </span>
                                        </div>
                                        <div className="eduman-course-tutor">
                                            <img src="assets/img/portfilo/course-tutor-02.png" alt="image not found"/>
                                            <Link href="/instructor-profile"><a><span>Mark Hanry</span></a></Link>
                                        </div>
                                        </div>
                                    </div>
                                    <div className="eduman-course-footer">
                                        <div className="course-lessson-svg">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                            viewBox="0 0 16.471 16.471">
                                            <g id="blackboar09154" transform="translate(-0.008)">
                                                <path id="Path_2501" data-name="Path 101"
                                                    d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                    fill="#575757" />
                                            </g>
                                        </svg>
                                        <span className="ms-2">12 Lessons</span>
                                        </div>
                                        <div className="course-deteals-btn">
                                            <Link href="/course-details"><a><span className="me-2">View Details</span><i className="far fa-arrow-right"></i></a></Link>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="tab-pane fade" id="nav-contactB" role="tabpanel" aria-labelledby="nav-contact-tabB">
                        <div className='row'>
                            <div className="col-xl-4 col-lg-4 col-md-6">
                                <div className="eduman-course-main-wrapper mb-30">
                                    <div className="course-cart">
                                        <div className="course-info-wrapper">
                                        <div className="cart-info-body">
                                            <span className="category-color category-color-1"><Link href="/course"><a>Development</a></Link></span>
                                            <Link href="/course-details"><a><h3>WordPress Development Course for Plugins and Themes</h3></a></Link>
                                            <div className="cart-lavel">
                                                <h5>Level : <span>Beginner</span></h5>
                                                <p>Knowledge is power. Information is liberating. Education is the premise of
                                                    progress, in every society, in every family</p>
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
                                    <div className="eduman-course-thumb w-img">
                                        <Link href="/course-details"><a><img src="assets/img/portfilo/course-img-01.jpg" alt="course-img"/></a></Link>
                                    </div>
                                    <div className="eduman-course-wraper">
                                        <div className="eduman-course-heading">
                                            <Link href="/course"><a className="course-link-color-1">Development</a></Link>
                                            <span className="couse-star"><i className="fas fa-star"></i>4.9 (25)</span>
                                        </div>
                                        <div className="eduman-course-text">
                                            <h3><Link href="/course-details"><a>WordPress Development Course for Plugins and Themes</a></Link></h3>
                                        </div>
                                        <div className="eduman-course-meta">
                                        <div className="eduman-course-price">
                                            <span className="price-now">$47.00 </span>
                                            <del className="price-old">$75.50</del>
                                        </div>
                                        <div className="eduman-course-tutor"><img src="assets/img/portfilo/course-tutor-01.png"
                                                alt="image not found"/>
                                            <Link href="/instructor-profile"><a><span>Danial</span></a></Link>
                                        </div>
                                        </div>
                                    </div>
                                    <div className="eduman-course-footer">
                                        <div className="course-lessson-svg">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16.471" height="16.471"
                                            viewBox="0 0 16.471 16.471">
                                            <g id="blackboar09" transform="translate(-0.008)">
                                                <path id="Path_01" data-name="Path 101"
                                                    d="M16,1.222H8.726V.483a.483.483,0,1,0-.965,0v.74H.491A.483.483,0,0,0,.008,1.7V13.517A.483.483,0,0,0,.491,14H5.24L4.23,15.748a.483.483,0,1,0,.836.483L6.354,14H7.761v1.99a.483.483,0,0,0,.965,0V14h1.407l1.288,2.231a.483.483,0,1,0,.836-.483L11.247,14H16a.483.483,0,0,0,.483-.483V1.7A.483.483,0,0,0,16,1.222Zm-.483.965v8.905H.973V2.187Zm0,10.847H.973v-.976H15.514Z"
                                                    fill="#575757" />
                                            </g>
                                        </svg>
                                        <span className="ms-2">12 Lessons</span>
                                        </div>
                                        <div className="course-deteals-btn">
                                            <Link href="/course-details"><a><span className="me-2">View Details</span><i className="far fa-arrow-right"></i></a></Link>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
         </div>
      </section>
    );
};

export default CourseTab;