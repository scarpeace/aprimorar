import React from "react";
import Link from "next/link";
import Breadcrumb from "../Common/Breadcrumb";

const InstructorProMain = () => {
  return (
    <main>
      <Breadcrumb
        breadcrumbTitle="Instructor"
        breadcrumbSubTitle="David Allberto"
      />

      <div className="course-detalies-area pt-120 pb-100">
        <div className="container">
          <div className="row">
            <div className="col-xl-3 col-lg-3">
              <div className="course-instructors-img mb-30">
                <img
                  src="assets/img/course/course-instructors.png"
                  alt="nstructors-img"
                />
              </div>
            </div>
            <div className="col-xl-8 col-lg-9">
              <div className="course-detelies-wrapper">
                <div className="course-detiles-tittle mb-30">
                  <h3>David Allberto</h3>
                  <span>Software Developer</span>
                </div>
                <div className="course-detiles-meta">
                  <div className="total-course">
                    <span>Total Courses</span>
                    <label>22</label>
                  </div>
                  <div className="student course">
                    <span>Students</span>
                    <label>5,230</label>
                  </div>
                  <div className="review-course">
                    <span>Review</span>
                    <div className="review-course-inner d-flex">
                      <ul>
                        <li>
                          <a href="#">
                            <i className="fas fa-star"></i>
                          </a>
                        </li>
                      </ul>
                      <p>4.9 (540)</p>
                    </div>
                  </div>
                  <div className="course-details-action">
                    <div className="course-follow">
                      <a href="#!" className="edu-follow-btn">
                        Follow
                      </a>
                    </div>
                    <div className="course-share">
                      <a href="#" className="share-btn">
                        <i className="far fa-share-alt"></i>
                      </a>
                    </div>
                  </div>
                </div>
                <div className="course-bio-text pt-45 pb-20">
                  <h3>Biography</h3>
                  <p>
                    David Allberto is a Software Developer and Instructor having
                    enjoyed his courses to date. He is the creator of Codexpand,
                    a place of learning and growth to help people move into and
                    be successful within the Helping Industry. One of Graham's
                    key driving forces is to remove the barriers to the Helping
                    Industry by producing high quality, accredited courses at
                    affordable prices.
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
};

export default InstructorProMain;
