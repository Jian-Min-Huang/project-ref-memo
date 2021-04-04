using System;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;

namespace CapitalGateway
{
    public class TcpServer
    {
        public TcpServer(CapitalApi capitalApi)
        {
            Console.WriteLine("[" + Thread.CurrentThread.Name + "] " + "本機名稱 = " + Dns.GetHostName());

            TcpListener tcpListener = new TcpListener(1235);
            tcpListener.Start();
            Console.WriteLine("[" + Thread.CurrentThread.Name + "] " + "等待客戶端連線中 ... ");

            TcpClient tcpClient;
            while (true)
            {
                try
                {
                    tcpClient = tcpListener.AcceptTcpClient();

                    if (tcpClient.Connected)
                    {
                        Console.WriteLine("[" + Thread.CurrentThread.Name + "] " + "連線成功 !");

                        HandleClient handleClient = new HandleClient(tcpClient, capitalApi);
                        Thread myThread = new Thread(handleClient.Communicate);
                        myThread.IsBackground = true;
                        myThread.Name = tcpClient.Client.RemoteEndPoint.ToString();
                        myThread.Start();
                    }
                }
                catch (Exception ex)
                {
                    Console.WriteLine(ex.Message);
                }
            }
        }
    }
    
    public class HandleClient
    {
        private TcpClient tcpClient;
        private CapitalApi capitalApi;

        public HandleClient(TcpClient tcpClient, CapitalApi capitalApi)
        {
            this.tcpClient = tcpClient;
            this.capitalApi = capitalApi;
        }

        public void Communicate()
        {
            try
            {
                CommunicationBase cb = new CommunicationBase();

                string msg;
                while ((msg = cb.ReceiveMsg(tcpClient)) != null)
                {
                    Console.Write("[" + Thread.CurrentThread.Name + "] " + msg);
                    if (msg == "HEART_BEAT\n")
                    {
                        cb.SendMsg("HEART_BEAT", tcpClient);
                    }
                    else if (msg == "DUMP_MIN_K\n")
                    {
                        cb.SendMsg("DUMP_MIN_K", tcpClient);
                        capitalApi.requestData("TSEA", 0);
                        capitalApi.writeFile("TSEA", "0");
                    }
                    else if (msg == "DUMP_DAY_K\n")
                    {
                        cb.SendMsg("DUMP_DAY_K", tcpClient);
                        capitalApi.requestData("TSEA", 4);
                        capitalApi.writeFile("TSEA", "4");
                    }
                    else if (msg.StartsWith("ORDER"))
                    {
                        cb.SendMsg("ORDER", tcpClient);
                    }
                    else
                    {
                        cb.SendMsg("Not Support Command : " + msg, tcpClient);
                    }   
                }
            }
            catch
            {
                Console.WriteLine("[" + Thread.CurrentThread.Name + "] " + "客戶端強制關閉連線!");
                tcpClient.Close();
            }
        }
    }

    public class CommunicationBase
    {
        public void SendMsg(string msg, TcpClient tmpTcpClient)
        {
            NetworkStream ns = tmpTcpClient.GetStream();
            if (ns.CanWrite)
            {
                byte[] msgByte = Encoding.Default.GetBytes(msg + "\n");
                ns.Write(msgByte, 0, msgByte.Length);
                ns.Flush();
            }
        }

        public string ReceiveMsg(TcpClient tmpTcpClient)
        {
            string receiveMsg = string.Empty;
            byte[] receiveBytes = new byte[tmpTcpClient.ReceiveBufferSize];
            int numberOfBytesRead = 0;
            NetworkStream ns = tmpTcpClient.GetStream();

            if (ns.CanRead)
            {
                do
                {
                    numberOfBytesRead = ns.Read(receiveBytes, 0, tmpTcpClient.ReceiveBufferSize);
                    receiveMsg = Encoding.Default.GetString(receiveBytes, 0, numberOfBytesRead);
                } while (ns.DataAvailable);
            }

            return receiveMsg;
        }
    }
}