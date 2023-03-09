import React from "react";
import Link from "next/link";

const BlogSection = () => {
  return (
    <div className="blog-area pt-110 pb-90">
      <div className="container">
        <div className="row justify-content-center">
          <div className="col-xl-12 text-center">
            <div className="section-title mb-55">
              <h2>
                Read Our <span className="down-mark-line-2">Latest</span> Tech
                News
              </h2>
            </div>
          </div>
        </div>
        <div className="row">
          <div className="col-xl-4 col-lg-4 col-md-6">
            <div className="blog-wrapper position-relative mb-30">
              <div className="blog-thumb ">
                <Link href="/blog-details">
                  <a>
                    <img src="assets/img/blog/blog-01.jpg" alt="blog-img" />
                  </a>
                </Link>
              </div>
              <Link href="/blog">
                <a className="blog-tag">
                  <i className="fal fa-folder-open"></i>Development
                </a>
              </Link>
              <div className="blog-content-wrapper">
                <div className="blog-meta">
                  <div className="blog-date">
                    <i className="flaticon-calendar"></i>
                    <span>23 Jan 2020</span>
                  </div>
                  <div className="blog-user">
                    <i className="flaticon-avatar"></i>
                    <span>Brian Hoff</span>
                  </div>
                </div>
                <div className="blog-content">
                  <Link href="/blog-details">
                    <a>
                      <h3>Ask the Expert: Typography Talk with Brian Hoff</h3>
                    </a>
                  </Link>
                  <Link href="/blog-details">
                    <a className="blog-btn">Read more</a>
                  </Link>
                </div>
              </div>
            </div>
          </div>
          <div className="col-xl-4 col-lg-4 col-md-6">
            <div className="blog-wrapper position-relative mb-30">
              <div className="blog-thumb ">
                <Link href="/blog-details">
                  <a>
                    <img src="assets/img/blog/blog-02.jpg" alt="blog-img" />
                  </a>
                </Link>
              </div>
              <Link href="/blog">
                <a className="blog-tag">
                  <i className="fal fa-folder-open"></i>Business
                </a>
              </Link>
              <div className="blog-content-wrapper">
                <div className="blog-meta">
                  <div className="blog-date">
                    <i className="flaticon-calendar"></i>
                    <span>20 Jan 2020</span>
                  </div>
                  <div className="blog-user">
                    <i className="flaticon-avatar"></i>
                    <span>Mark Hanry</span>
                  </div>
                </div>
                <div className="blog-content">
                  <Link href="/blog-details">
                    <a>
                      <h3>
                        Stop Redesigning And Start Your Tuning Your Site Instead
                      </h3>
                    </a>
                  </Link>
                  <Link href="/blog-details">
                    <a className="blog-btn">Read more</a>
                  </Link>
                </div>
              </div>
            </div>
          </div>
          <div className="col-xl-4 col-lg-4 col-md-6">
            <div className="blog-wrapper position-relative mb-30">
              <div className="blog-thumb ">
                <Link href="/blog-details">
                  <a>
                    <img src="assets/img/blog/blog-03.jpg" alt="blog-img" />
                  </a>
                </Link>
              </div>
              <Link href="/blog">
                <a className="blog-tag">
                  <i className="fal fa-folder-open"></i>Web Design
                </a>
              </Link>
              <div className="blog-content-wrapper">
                <div className="blog-meta">
                  <div className="blog-date">
                    <i className="flaticon-calendar"></i>
                    <span>18 Jan 2020</span>
                  </div>
                  <div className="blog-user">
                    <i className="flaticon-avatar"></i>
                    <span>Eduman</span>
                  </div>
                </div>
                <div className="blog-content">
                  <Link href="/blog-details">
                    <a>
                      <h3>How To Teach Web Design to the New Students</h3>
                    </a>
                  </Link>
                  <Link href="/blog-details">
                    <a className="blog-btn">Read more</a>
                  </Link>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default BlogSection;
