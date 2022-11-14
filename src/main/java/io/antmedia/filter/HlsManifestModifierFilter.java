package io.antmedia.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.ContentCachingResponseWrapper;

import io.lindstrom.m3u8.model.MediaPlaylist;
import io.lindstrom.m3u8.model.MediaSegment;
import io.lindstrom.m3u8.parser.MediaPlaylistParser;

public class HlsManifestModifierFilter implements Filter {

	protected static Logger logger = LoggerFactory.getLogger(HlsManifestModifierFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {


		HttpServletRequest httpRequest =(HttpServletRequest)request;
		
		String method = httpRequest.getMethod();
		if (HttpMethod.GET.equals(method) && httpRequest.getRequestURI().endsWith("m3u8")) {
			//only accept GET methods
			String sessionId = httpRequest.getSession().getId();

			String startDate = ((HttpServletRequest) request).getParameter("start");
			String endDate = ((HttpServletRequest) request).getParameter("end");
			
			long start = Long.parseLong(startDate);
			long end = Long.parseLong(endDate);


			ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

					
			chain.doFilter(request, responseWrapper);

			int status =responseWrapper.getStatus();

			if (HttpServletResponse.SC_OK <= status && status <= HttpServletResponse.SC_BAD_REQUEST) 
			{				
				try {
		            // Get the original response data
		            final byte[] originalData = responseWrapper.getContentAsByteArray();
		            final int originalLength = responseWrapper.getContentSize();
		            
		            String original = new String(originalData);
		            
		            MediaPlaylistParser parser = new MediaPlaylistParser();
		            MediaPlaylist playList = parser.readPlaylist(original);
		            
		            List<MediaSegment> segments = new ArrayList<MediaSegment>();

		            for (MediaSegment s : playList.mediaSegments()) {
		            	long time = s.programDateTime().get().toEpochSecond();
		            	if(time >= start && time <= end) {
		            		segments.add(MediaSegment.builder()
		            				.duration(s.duration())
		            				.uri(s.uri())
		            				.build());
		            	}
					}
		            
		            MediaPlaylist newPlayList = MediaPlaylist.builder()
                    .version(playList.version())
                    .targetDuration(playList.targetDuration())
                    .ongoing(false)
                    .addAllMediaSegments(segments)
                    .build();
		            
		            // Modify the original data
		            MediaPlaylistParser parser2 = new MediaPlaylistParser();

		            final String newData = parser2.writePlaylistAsString(newPlayList);

		            // Write the data into the output stream
		            response.setContentLength(newData.length());
		            response.getOutputStream().write(newData.getBytes());

		            // Commit the written data
		            response.getWriter().flush();

		        } catch (Exception e) {
		            response.setContentLength(responseWrapper.getContentSize());
		            response.getOutputStream().write(responseWrapper.getContentAsByteArray());
		            response.flushBuffer();

		        } finally {
		            //NOOP
		        }
			}
		}
		else {
			chain.doFilter(httpRequest, response);
		}

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}


}
