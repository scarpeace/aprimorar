import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import Link from 'next/link';
import BlogSidebar from '../Blog/BlogSidebar';

const BlogDetails = () => {
    const blogItem = useSelector(state => state.blogs.specificItem);

    return (
        <main>
            <div className="hero-arera course-item-height" style={{background:"url(assets/img/slider/course-slider.jpg)"}}>
                <div className="container">
                    <div className="row">
                        <div className="col-xl-12">
                            <div className="hero-course-1-text">
                                <h2>{blogItem.title}</h2>
                            </div>
                            <div className="course-title-breadcrumb">
                                <nav>
                                <ol className="breadcrumb">
                                    <li className="breadcrumb-item"><Link href="/"><a>Home</a></Link></li>
                                    <li className="breadcrumb-item"><span>{blogItem.title}</span></li>
                                </ol>
                                </nav>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div className="blog-area pt-120 pb-90">
                <div className="container">
                    <div className="row">
                    <div className="col-xl-8 col-lg-12">
                        <div className="blog-main-wrapper mb-0">
                            <div className="row">
                                <div className="col-xl-12 col-lg-12 col-md-12">
                                <div className="blog-wrapper position-relative blog-details-wrapper mb-30">
                                    <div className="blog-thumb ">
                                        <img src={blogItem.img} alt="blog-img"/>
                                    </div>
                                    <div className="blog-tag">
                                        <i className="fal fa-folder-open"></i>
                                        <a href="#">{blogItem.category}</a>
                                    </div>
                                    <div className="blog-content-wrapper">
                                        <div className="blog-meta">
                                            <div className="blog-date">
                                            <i className="flaticon-calendar"></i>
                                            <span>{blogItem.date}</span>
                                            </div>
                                            <div className="blog-user">
                                            <i className="flaticon-avatar"></i>
                                            <span>{blogItem.name}</span>
                                            </div>
                                        </div>
                                        <div className="blog-content">
                                            <p>When it comes to designing better links and sending better emails, Slava
                                            Shestopalov has a few tips on how to improve
                                            your websites experience while accessibility in mind. There are so many websites
                                            out there that have not considered the overall
                                            usability of their visually impaired users.
                                            The participants will get
                                            general ideas of the land management
                                            system of business. Everyone must work, but for many of us that job do not just a
                                            paycheck, it is an opportunity to express ourselves and make
                                            something better. Entrepreneurs and go-getters often feel as if they carry the
                                            weight of an entire organization on their
                                            backs, and therefore could always use a little extra motivation.</p>
                                            <blockquote>
                                            <p>Tosser argy-bargy mush loo at public school Elizabeth up the duff buggered
                                                chinwag on your bike mate do not get
                                                shirty with me super, Jeffrey bobby Richard cheesed off spend a penny a load
                                                of old tosh blag horse.</p>
                                            <p className="mb-0"><cite>Eduman</cite></p>
                                            </blockquote>
                                            <p>Cheeky bugger cracking goal starkers lemon squeezy lost the plot pardon me no
                                            biggie the BBC burke gosh boot so I
                                            said wellies, zonked a load of old tosh bodge barmy skive off he legged it morish
                                            spend a penny my good sir wind up
                                            hunky-dory. Naff grub elizabeth cheesed off do not get shirty with me arse over
                                            tit mush a blinding shot young
                                            delinquent bloke boot blatant.</p>
                                            <p><img src="assets/img/blog/blog-07.jpg" alt="img"/></p>
                                            <h4 className="mb-20">Typography is the powerful element you will ever need</h4>
                                            <p>The participants will get
                                            general ideas of the land management
                                            system of business. Everyone must work, but for many of us that job is not just a
                                            paycheck, it is an opportunity to express ourselves and make
                                            something better. Entrepreneurs and go-getters often feel as if they carry the
                                            weight of an entire organization on their
                                            backs, and therefore could always use a little extra motivation.</p>
                                            <p>When it comes to designing better links and sending better emails, Slava
                                            Shestopalov has a few tips on how to improve
                                            your websites experience while accessibility in mind. There are so many websites
                                            out there that have not considered the overall
                                            usability of their visually impaired users.</p>
                                            <div className="blog__details__tag tagcloud"><span>Post Tags : </span>
                                            <a href="#" rel="tag">Class</a>
                                            <a href="#" rel="tag">Course</a>
                                            <a href="#" rel="tag">Design</a>
                                            <a href="#" rel="tag">Science</a>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="latest-comments mb-50 mt-50">
                                        <h3>3 Comments</h3>
                                        <ul>
                                            <li>
                                            <div className="comments-box">
                                                <div className="comments-info d-flex">
                                                    <div className="comments-avatar mr-20">
                                                        <img src="assets/img/member/member-img-04.png" alt=""/>
                                                    </div>
                                                    <div className="avatar-name">
                                                        <h5>Eleanor Fant</h5>
                                                        <span className="post-meta"> July 14, 2022</span>
                                                    </div>
                                                </div>
                                                <div className="comments-text ml-65">
                                                    <p>So I said lurgy dropped a clanger Jeffrey bugger cuppa gosh David
                                                        blatant have it, standard A bit of
                                                        how is your father my lady absolutely.</p>
                                                    <div className="comments-replay">
                                                        <a href="#">Reply</a>
                                                    </div>
                                                </div>
                                            </div>
                                            </li>
                                            <li className="children">
                                            <div className="comments-box">
                                                <div className="comments-info d-flex">
                                                    <div className="comments-avatar mr-20">
                                                        <img src="assets/img/member/member-img-01.png" alt=""/>
                                                    </div>
                                                    <div className="avatar-name">
                                                        <h5>Dominic</h5>
                                                        <span className="post-meta">April 16, 2022 </span>
                                                    </div>
                                                </div>
                                                <div className="comments-text ml-65">
                                                    <p>David blatant have it, standard A bit of how is your father my lady
                                                        absolutely.</p>
                                                    <div className="comments-replay">
                                                        <a href="#">Reply</a>
                                                    </div>
                                                </div>
                                            </div>
                                            <ul>
                                                <li className="children-2">
                                                    <div className="comments-box">
                                                        <div className="comments-info d-flex">
                                                        <div className="comments-avatar mr-20">
                                                            <img src="assets/img/member/member-img-02.png" alt=""/>
                                                        </div>
                                                        <div className="avatar-name">
                                                            <h5>Von Rails</h5>
                                                            <span className="post-meta">April 18, 2022 </span>
                                                        </div>
                                                        </div>
                                                        <div className="comments-text ml-65">
                                                        <p>He nicked it get stuffed mate spend a penny plastered.!</p>
                                                        <div className="comments-replay">
                                                            <a href="#">Reply</a>
                                                        </div>
                                                        </div>
                                                    </div>
                                                </li>
                                            </ul>
                                            </li>
                                        </ul>
                                        <ul>
                                            <li>
                                            <div className="comments-box">
                                                <div className="comments-info d-flex">
                                                    <div className="comments-avatar mr-20">
                                                        <img src="assets/img/member/member-img-04.png" alt=""/>
                                                    </div>
                                                    <div className="avatar-name">
                                                        <h5>Eleanor Fant</h5>
                                                        <span className="post-meta"> July 14, 2022</span>
                                                    </div>
                                                </div>
                                                <div className="comments-text ml-65">
                                                    <p>So I said lurgy dropped a clanger Jeffrey bugger cuppa gosh David
                                                        blatant have it, standard A bit of
                                                        how is your father my lady absolutely.</p>
                                                    <div className="comments-replay">
                                                        <a href="#">Reply</a>
                                                    </div>
                                                </div>
                                            </div>
                                            </li>
                                            <li className="children">
                                            <div className="comments-box">
                                                <div className="comments-info d-flex">
                                                    <div className="comments-avatar mr-20">
                                                        <img src="assets/img/member/member-img-01.png" alt=""/>
                                                    </div>
                                                    <div className="avatar-name">
                                                        <h5>Dominic</h5>
                                                        <span className="post-meta">April 16, 2022 </span>
                                                    </div>
                                                </div>
                                                <div className="comments-text ml-65">
                                                    <p>David blatant have it, standard A bit of how is your father my lady
                                                        absolutely.</p>
                                                    <div className="comments-replay">
                                                        <a href="#">Reply</a>
                                                    </div>
                                                </div>
                                            </div>
                                            </li>
                                        </ul>
                                    </div>
                                    <div className="blog__comment">
                                        <h3>Leave a Comment</h3>
                                        <form action="#">
                                            <div className="row">
                                            <div className="col-xxl-6 col-xl-6 col-lg-6">
                                                <div className="blog__comment-input">
                                                    <input type="text" placeholder="Your Name"/>
                                                </div>
                                            </div>
                                            <div className="col-xxl-6 col-xl-6 col-lg-6">
                                                <div className="blog__comment-input">
                                                    <input type="email" placeholder="Your Email"/>
                                                </div>
                                            </div>
                                            <div className="col-xxl-12">
                                                <div className="blog__comment-input">
                                                    <input type="text" placeholder="Website"/>
                                                </div>
                                            </div>
                                            <div className="col-xxl-12">
                                                <div className="blog__comment-input">
                                                    <textarea placeholder="Enter your comment ..."></textarea>
                                                </div>
                                            </div>
                                            <div className="col-xxl-12">
                                                <div className="blog__comment-agree d-flex align-items-center mb-20">
                                                    <input className="e-check-input" type="checkbox" id="e-agree"/>
                                                    <label className="e-check-label" htmlFor="e-agree">Save my name, email, and website
                                                        in this browser for the next
                                                        time I comment.</label>
                                                </div>
                                            </div>
                                            <div className="col-xxl-12">
                                                <div className="blog__comment-btn">
                                                    <button type="submit" className="edu-btn">Post Comment</button>
                                                </div>
                                            </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                                </div>
                            </div>

                        </div>

                    </div>
                    <div className="col-xl-4 col-lg-8 col-md-8">
                        <BlogSidebar />
                    </div>
                    </div>
                </div>
            </div>
        </main>
    );
};

export default BlogDetails;