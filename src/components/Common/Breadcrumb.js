import Link from 'next/link';
import React from 'react';

const Breadcrumb = ({ breadcrumbTitle, breadcrumbSubTitle }) => {
   return (
        <div className="hero-arera course-item-height" style={{background:"url(assets/img/slider/course-slider.jpg)"}}>
            <div className="container">
                <div className="row">
                    <div className="col-xl-12">
                        <div className="hero-course-1-text">
                            <h2>{breadcrumbTitle}</h2>
                        </div>
                        <div className="course-title-breadcrumb">
                            <nav>
                            <ol className="breadcrumb">
                                <li className="breadcrumb-item"><Link href="/"><a>Home</a></Link></li>
                                <li className="breadcrumb-item"><span>{breadcrumbSubTitle}</span></li>
                            </ol>
                            </nav>
                        </div>
                    </div>
                </div>
            </div>
        </div>
   );
};

export default Breadcrumb;