import React from 'react';

const Pagination = () => {
    return (
        <div className="edu-pagination mt-30 mb-20">
            <ul>
                <li><a href="#"><i className="fal fa-angle-left"></i></a></li>
                <li className="active"><a href="#"><span>01</span></a></li>
                <li><a href="#"><span>02</span></a></li>
                <li><a href="#"><i className="fal fa-angle-right"></i></a></li>
            </ul>
        </div>
    );
};

export default Pagination;