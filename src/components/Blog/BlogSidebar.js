import React from 'react';
import Link from 'next/link';
import { useDispatch, useSelector } from 'react-redux';
import { specificBlog } from '../../redux/features/blogSlice';

const BlogSidebar = () => {
    const sidebarBlogs = useSelector(state => state.blogs.blogs);
    const dispatch = useDispatch();
    return (
        <div className="sidebar-widget-wrapper">
            <div className="sidebar__search p-relative mb-30">
                <form action="#">
                    <input type="text" placeholder="Search for courses..."/>
                    <button type="submit">
                        <i className="far fa-search"></i>
                    </button>
                </form>
            </div>
            <div className="sidebar__widget mb-30">
                <div className="sidebar__widget-head mb-35">
                    <h4 className="sidebar__widget-title">Recent posts</h4>
                </div>
                <div className="sidebar__widget-content">
                    <div className="rc__post-wrapper">
                        {
                            sidebarBlogs.slice(3, 6).map(blog => {
                                return <div key={blog.id} className="rc__post d-flex align-items-center">
                                   <div className="rc__thumb mr-20" onClick={()=> dispatch(specificBlog(blog.id))}>
                                      <Link href="/blog-details">
                                         <a ><img src={blog.sm_img} alt="" /></a>
                                      </Link>
                                   </div>
                                   <div className="rc__content">
                                      <div className="rc__meta">
                                         <span>{blog.date}</span>
                                      </div>
                                      <h6 className="rc__title" onClick={()=> dispatch(specificBlog(blog.id))}>
                                         <Link href="/blog-details">
                                            <a >{blog.title.substring(0,40)}...</a>
                                         </Link>
                                      </h6>
                                   </div>
                                </div>
                             })
                        }
                    </div>
                </div>
            </div>
            <div className="sidebar__widget mb-30">
            <div className="sidebar__widget-head mb-35">
                <h4 className="sidebar__widget-title">Category</h4>
            </div>
            <div className="sidebar__widget-content">
                <div className="sidebar__category">
                    <ul>
                        <li><Link href="/blog"><a>Data Science(2)</a></Link></li>
                        <li><Link href="/blog"><a>Video and Tips (4)</a></Link></li>
                        <li><Link href="/blog"><a>Education (8)</a></Link></li>
                        <li><Link href="/blog"><a>Business (5)</a></Link></li>
                        <li><Link href="/blog"><a>UX Design (3)</a></Link></li>
                    </ul>
                </div>
            </div>
            </div>
            <div className="sidebar__widget mb-30">
            <div className="sidebar__widget-head mb-35">
                <h4 className="sidebar__widget-title">Tags</h4>
            </div>
            <div className="sidebar__widget-content">
                <div className="sidebar__tag">
                    <Link href="/blog"><a>Art and Design</a></Link>
                    <Link href="/blog"><a>Course</a></Link>
                    <Link href="/blog"><a>Videos</a></Link>
                    <Link href="/blog"><a>App</a></Link>
                    <Link href="/blog"><a>Education</a></Link>
                    <Link href="/blog"><a>Data Science</a></Link>
                    <Link href="/blog"><a>Machine Learning</a></Link>
                    <Link href="/blog"><a>Tips</a></Link>
                </div>
            </div>
            </div>
        </div>
    );
};

export default BlogSidebar;