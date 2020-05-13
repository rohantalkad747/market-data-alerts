package com.h2o_execution.streams;

import com.h2o_execution.domain.Quote;
import lombok.Data;
import org.apache.flink.streaming.api.datastream.DataStreamSource;

@Data
public class FeedFormattingProcessor implements IFeedFilter<Quote, String>
{
    private DataStreamSource<String> inputStream;

    @Override
    public void applyFilter()
    {
        this.inputStream
                .map(value ->
                {
                    String[] tokens = value.split(",");
                    return new Quote(tokens[0], Double.parseDouble(tokens[1]));
                });
    }
}
